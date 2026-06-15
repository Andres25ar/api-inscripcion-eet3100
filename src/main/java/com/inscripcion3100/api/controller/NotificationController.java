package com.inscripcion3100.api.controller;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.notification.NotificationRequestDTO;
import com.inscripcion3100.api.dto.notification.NotificationResponseDTO;
import com.inscripcion3100.api.service.INotificationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'SECRETARIO')")
    public ResponseEntity<MessageResponse> createNotification (
            @Valid @RequestBody NotificationRequestDTO request,
            Authentication auth
            ){
        String userSenderEmail = auth.getName();
        MessageResponse response = notificationService.sendNotification(request, userSenderEmail);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications (Authentication auth){
        String userName = auth.getName();
        List<NotificationResponseDTO> unreadNotifications = notificationService.getMyUnreadNotifications(userName);
        return ResponseEntity.ok(unreadNotifications);
    }

    @PutMapping("/{idUserNotification}/read")
    public void readNotification (
            @PathVariable("idUserNotification")Long idUserNotification,
            Authentication auth){
        String userEmail = auth.getName();
        notificationService.markAsRead(idUserNotification, userEmail);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> countNotifications (Authentication auth){
        String userEmail = auth.getName();
        Long cnt = notificationService.countUnreadNotifications(userEmail);
        return ResponseEntity.ok(cnt);
    }
}
