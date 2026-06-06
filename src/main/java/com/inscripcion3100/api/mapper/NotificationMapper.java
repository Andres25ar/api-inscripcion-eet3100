package com.inscripcion3100.api.mapper;

import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.notification.NotificationRequestDTO;
import com.inscripcion3100.api.dto.notification.NotificationResponseDTO;
import com.inscripcion3100.api.entity.Notification;
import com.inscripcion3100.api.entity.NotificationForUser;
import com.inscripcion3100.api.entity.Student;
import com.inscripcion3100.api.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationMapper {
    public static NotificationResponseDTO toNotificacionDTO (Notification notification) {
        if (notification == null) {
            return null;
        }

        StaffResponseDTO sender = UserMapper.toStaffDto(notification.getSender());

        return new NotificationResponseDTO(
                notification.getNotificationId(),
                notification.getNotificationDate(),
                notification.getContent(),
                sender
        );
    }

    public static Notification toNotificationEntity(NotificationRequestDTO notificationDTO, User sender) {
        if (notificationDTO == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setContent(notificationDTO.getContent());
        notification.setNotificationDate(new Date());
        notification.setSender(sender);

        return notification;
    }

    public static NotificationForUser toNotificationForUserEntity(Notification notification, User receiver) {
        if (notification == null || receiver == null) {
            return null;
        }

        NotificationForUser notifForUser = new NotificationForUser();
        notifForUser.setNotification(notification);
        notifForUser.setReceiver(receiver);
        notifForUser.setIsRead(false);

        return notifForUser;
    }
}
