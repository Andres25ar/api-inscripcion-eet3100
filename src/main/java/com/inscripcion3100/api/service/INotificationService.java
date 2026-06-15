package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.notification.NotificationRequestDTO;
import com.inscripcion3100.api.dto.notification.NotificationResponseDTO;

import java.util.List;

public interface INotificationService {
    MessageResponse sendNotification(NotificationRequestDTO request, String senderEmail);

    List<NotificationResponseDTO> getMyUnreadNotifications(String userEmail);

    void markAsRead(Long idUserNotification, String userEmail);

    Long countUnreadNotifications(String userEmail);
}
