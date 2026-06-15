package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.notification.NotificationRequestDTO;
import com.inscripcion3100.api.dto.notification.NotificationResponseDTO;
import com.inscripcion3100.api.entity.Notification;
import com.inscripcion3100.api.entity.NotificationForUser;
import com.inscripcion3100.api.entity.Role;
import com.inscripcion3100.api.entity.User;
import com.inscripcion3100.api.exception.ResourceNotFoundException;
import com.inscripcion3100.api.mapper.NotificationMapper;
import com.inscripcion3100.api.repository.NotificationForUserRepository;
import com.inscripcion3100.api.repository.NotificationRepository;
import com.inscripcion3100.api.repository.UserRepository;
import com.inscripcion3100.api.service.INotificationService;
import org.apache.tomcat.util.modeler.NotificationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationForUserRepository notXUserRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            NotificationForUserRepository notXUserRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.notXUserRepository = notXUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public MessageResponse sendNotification(NotificationRequestDTO request, String senderEmail) {
        User sender = userRepository.findByUserEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "senderEmail", senderEmail));

        if(sender.getRole() == Role.TUTOR){
            throw new IllegalArgumentException ("Violacion de seguridad... Usuario no habilitado para enviar notificaciones");
        }

        User receiver = userRepository.findById(request.getUserNotified())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", request.getUserNotified()));

        Notification notification = new Notification();
        NotificationForUser notificationForUser = new NotificationForUser();

        //guardar datos de la notificacion
        notification.setContent(request.getContent());
        notification.setNotificationDate(new Date());
        notification.setSender(sender);
        //datos de usuario notificado
        notificationForUser.setIsRead(false);
        notificationForUser.setNotification(notification);
        notificationForUser.setReceiver(receiver);

        Notification notificationSaved = notificationRepository.save(notification);
        NotificationForUser notificationForUserSaved = notXUserRepository.save(notificationForUser);

        return new MessageResponse("Notificacion enviada");
    }

    @Override
    @Transactional
    public List<NotificationResponseDTO> getMyUnreadNotifications(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", userEmail));

        List<NotificationForUser> unreadNotifications = notXUserRepository.findByReceiverAndIsReadFalse(user);

        return unreadNotifications.stream()
                .map(notifUser -> notifUser.getNotification())
                .map(NotificationMapper::toNotificacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long idUserNotification, String userEmail) {
        NotificationForUser notification = notXUserRepository.findById(idUserNotification)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationForUser", "idUserNotification", idUserNotification));

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if(!notification.getReceiver().getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("Violacion de seguridad: No tiene permiso para acceder a esta notificacion");
        }

        notification.setIsRead(true);
        notXUserRepository.save(notification);
    }

    @Override
    public Long countUnreadNotifications(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", userEmail));

        Long unread = notXUserRepository.countByReceiverAndIsReadFalse(user);

        return unread;
    }
}
