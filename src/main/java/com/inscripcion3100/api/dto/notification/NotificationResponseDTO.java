package com.inscripcion3100.api.dto.notification;

import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Date date;
    private String content;
    private StaffResponseDTO sender;
}
