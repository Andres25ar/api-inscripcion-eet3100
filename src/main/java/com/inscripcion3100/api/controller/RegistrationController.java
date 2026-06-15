package com.inscripcion3100.api.controller;

import com.inscripcion3100.api.dto.inscription.InscriptionRequestDTO;
import com.inscripcion3100.api.dto.inscription.InscriptionResponseDTO;
import com.inscripcion3100.api.service.IRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    private final IRegistrationService registrationService;

    public RegistrationController(IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<InscriptionResponseDTO> createRegistration(
            @Valid @RequestBody InscriptionRequestDTO request,
            Authentication auth) {
        String tutorEmail = auth.getName();
        InscriptionResponseDTO response = registrationService.createRegistration(request, tutorEmail);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<Void> approveRegistration(@PathVariable("id") Long registrationId) {
        registrationService.approveRegistration(registrationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/propose-reassignment")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<Void> proposeReassignment(
            @PathVariable("id") Long registrationId,
            @RequestParam("newCourseId") Long newCourseId) {
        registrationService.proposeReassignment(registrationId, newCourseId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<Void> rejectRegistration(@PathVariable("id") Long registrationId) {
        registrationService.rejectRegistration(registrationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reply-reassignment")
    public ResponseEntity<Void> replyToReassignment(
            @PathVariable("id") Long registrationId,
            @RequestParam("accepted") boolean accepted,
            Authentication auth) {
        String userEmail = auth.getName();
        registrationService.replyToReassignment(registrationId, accepted, userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<List<InscriptionResponseDTO>> getPendingRegistrations() {
        List<InscriptionResponseDTO> response = registrationService.getPendingRegistrations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<List<InscriptionResponseDTO>> getRegistrationsByCourse(
            @PathVariable("courseId") Long courseId) {
        List<InscriptionResponseDTO> response = registrationService.getRegistrationsByCourse(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<InscriptionResponseDTO>> getMyRegistrations(Authentication auth) {
        String tutorEmail = auth.getName();
        List<InscriptionResponseDTO> response = registrationService.getMyRegistrations(tutorEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentDni}/year/{year}")
    public ResponseEntity<InscriptionResponseDTO> getRegistrationByStudentDniAndYear(
            @PathVariable("studentDni") Long studentDni,
            @PathVariable("year") Integer year) {
        InscriptionResponseDTO response = registrationService.getRegistrationByStudentDniAndYear(studentDni, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/latest")
    public ResponseEntity<InscriptionResponseDTO> getLatestRegistrationByStudent(
            @PathVariable("studentId") Long studentId,
            Authentication auth) {
        String username = auth.getName();
        InscriptionResponseDTO response = registrationService.getLatestRegistrationByStudent(studentId, username);
        return ResponseEntity.ok(response);
    }
}
