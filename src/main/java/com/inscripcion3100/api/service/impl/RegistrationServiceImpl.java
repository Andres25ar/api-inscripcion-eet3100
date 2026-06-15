package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.inscription.InscriptionRequestDTO;
import com.inscripcion3100.api.dto.inscription.InscriptionResponseDTO;
import com.inscripcion3100.api.dto.notification.NotificationRequestDTO;
import com.inscripcion3100.api.entity.*;
import com.inscripcion3100.api.exception.ResourceNotFoundException;
import com.inscripcion3100.api.mapper.RegistrationMapper;
import com.inscripcion3100.api.repository.CourseRepository;
import com.inscripcion3100.api.repository.RegistrationApplicationRepository;
import com.inscripcion3100.api.repository.StudentRepository;
import com.inscripcion3100.api.repository.UserRepository;
import com.inscripcion3100.api.service.INotificationService;
import com.inscripcion3100.api.service.IRegistrationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImpl implements IRegistrationService {

    private final RegistrationApplicationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final INotificationService notificationService;

    public RegistrationServiceImpl(
            RegistrationApplicationRepository registrationRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            UserRepository userRepository,
            INotificationService notificationService) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public InscriptionResponseDTO createRegistration(InscriptionRequestDTO request, String tutorEmail) {
        User tutor = userRepository.findByUserEmail(tutorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", tutorEmail));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        if (!student.getTutor1().getUserId().equals(tutor.getUserId())) {
            throw new IllegalArgumentException("Violación de seguridad: No tiene permisos para inscribir a este alumno porque no está a su cargo.");
        }

        Course course = courseRepository.findById(request.getIdCourse())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Boolean alreadyRegistered = registrationRepository.existsByStudentAndCourseYear(student, course.getYear());
        if (alreadyRegistered) {
            throw new RuntimeException("El alumno ya posee una inscripción para el ciclo lectivo de este curso");
        }

        RegistrationApplication registration = RegistrationMapper.toRegistrationApplication(request, course, student);

        RegistrationApplication savedRegistration = registrationRepository.save(registration);

        return RegistrationMapper.toInscriptionResponseDTO(savedRegistration);
    }

    @Override
    @Transactional(readOnly = true)
    public InscriptionResponseDTO getRegistrationByStudentDniAndYear(Long studentDni, Integer year) {
        RegistrationApplication registration = registrationRepository
                .findFirstByStudent_StudentDniAndCourse_YearOrderByRegistrationDateDesc(studentDni, year)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró inscripción para el alumno en el año especificado"));

        return RegistrationMapper.toInscriptionResponseDTO(registration);
    }

    @Override
    @Transactional
    public void approveRegistration(Long registrationId) {
        RegistrationApplication registration = registrationRepository.findById(registrationId).orElseThrow();

        if (registration.getStatus() == RegistrationStatus.APPROVED) {
            throw new IllegalArgumentException("Esta inscripción ya se encuentra aprobada.");
        }

        Course course = registration.getCourse();

        if (course.getAvailablePlaces() <= 0) {
            throw new RuntimeException("No hay cupos disponibles. Intente reasignar al alumno de turno.");
        }

        course.setAvailablePlaces(course.getAvailablePlaces() - 1);
        courseRepository.save(course);

        registration.setStatus(RegistrationStatus.APPROVED);
        registrationRepository.save(registration);

        notifyTutor(registration.getStudent().getTutor1().getUserId(),
                "La inscripción de " + registration.getStudent().getFirstName() + " al curso " + course.getStudyYear() + "° " + course.getDivision() + "° ha sido APROBADA.");
    }

    @Override
    @Transactional
    public void proposeReassignment(Long registrationId, Long newCourseId) {
        RegistrationApplication registration = registrationRepository.findById(registrationId).orElseThrow();
        Course newCourse = courseRepository.findById(newCourseId).orElseThrow();

        registration.setCourse(newCourse);
        registration.setStatus(RegistrationStatus.REASSIGNED);
        registrationRepository.save(registration);

        notifyTutor(registration.getStudent().getTutor1().getUserId(),
                "El alumno " + registration.getStudent().getFirstName() + " ha sido propuesto para reasignación al curso " + newCourse.getStudyYear() + "° " + newCourse.getDivision() + "°. Por favor, confirme si acepta el cambio en la aplicación.");
    }

    @Override
    @Transactional
    public void rejectRegistration(Long registrationId) {
        RegistrationApplication registration = registrationRepository.findById(registrationId).orElseThrow();

        if (registration.getStatus() == RegistrationStatus.APPROVED) {
            Course course = registration.getCourse();
            course.setAvailablePlaces(course.getAvailablePlaces() + 1);
            courseRepository.save(course);
        }

        registration.setStatus(RegistrationStatus.REJECTED);
        registrationRepository.save(registration);

        notifyTutor(registration.getStudent().getTutor1().getUserId(),
                "La inscripción de " + registration.getStudent().getFirstName() + " ha sido RECHAZADA.");
    }

    @Override
    @Transactional
    public void replyToReassignment(Long registrationId, boolean accepted, String userEmail) {
        RegistrationApplication registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if(!registration.getStudent().getTutor1().getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("Violacion de seguridad: No puede responder a esta solicitud");
        }

        if (registration.getStatus() != RegistrationStatus.REASSIGNED) {
            throw new IllegalArgumentException("La inscripción no está pendiente de reasignación.");
        }

        if (accepted) {
            Course course = registration.getCourse();
            if (course.getAvailablePlaces() <= 0) {
                throw new RuntimeException("Ya no quedan cupos en el curso reasignado.");
            }
            course.setAvailablePlaces(course.getAvailablePlaces() - 1);
            courseRepository.save(course);

            registration.setStatus(RegistrationStatus.APPROVED);
        } else {
            registration.setStatus(RegistrationStatus.REJECTED);
        }
        registrationRepository.save(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getPendingRegistrations() {
        List<RegistrationApplication> registrationApplications = registrationRepository.findByStatus(RegistrationStatus.PENDING);

        return registrationApplications.stream()
                .map(RegistrationMapper::toInscriptionResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getRegistrationsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        List<RegistrationApplication> registrations = registrationRepository.findByCourse(course);

        return registrations.stream()
                .map(RegistrationMapper::toInscriptionResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> getMyRegistrations(String tutorEmail) {
        User tutor = userRepository.findByUserEmail(tutorEmail)
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));

        List<Student> students = studentRepository.findByTutor1(tutor);
        List<InscriptionResponseDTO> tutorRegistrations = new ArrayList<>();

        for (Student student : students) {
            List<RegistrationApplication> studentRegistrations = registrationRepository.findByStudent(student);
            tutorRegistrations.addAll(
                    studentRegistrations.stream()
                            .map(RegistrationMapper::toInscriptionResponseDTO)
                            .collect(Collectors.toList())
            );
        }

        return tutorRegistrations;
    }

    private void notifyTutor(Long tutorId, String content) {
        String staffEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        NotificationRequestDTO notifRequest = new NotificationRequestDTO();
        notifRequest.setContent(content);
        notifRequest.setUserNotified(tutorId);
        notificationService.sendNotification(notifRequest, staffEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public InscriptionResponseDTO getLatestRegistrationByStudent(Long studentId, String userEmail) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("El alumno no encontrado"));

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if(!student.getTutor1().getUserId().equals(user.getUserId())
                && user.getRole()!=Role.SECRETARIO
                && user.getRole()!=Role.ADMINISTRADOR){
            throw new IllegalArgumentException("Violacion de seguridad: No puede acceder a los datos de este alumno");
        }

        RegistrationApplication latestReg = registrationRepository
                .findFirstByStudentOrderByRegistrationDateDesc(student)
                .orElseThrow(() -> new ResourceNotFoundException("El alumno no tiene inscripciones previas"));

        return RegistrationMapper.toInscriptionResponseDTO(latestReg);
    }
}