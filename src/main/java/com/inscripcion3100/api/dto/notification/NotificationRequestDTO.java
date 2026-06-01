package com.inscripcion3100.api.dto.notification;

import com.inscripcion3100.api.dto.user.UserProfileDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    //private Date date;

    @NotNull(message = "Especifique lo que quiere notificar")
    @Size(min = 10, max =  800)
    private String content;

    //para notificar a todos los padre de un curso
    private Long targetCourseId;

    //si se quiere notificar a un usuario especifico (quizas solo basta con el correo o el id)
    private Long userNotified;

    @NotNull(message = "Es necesario saber si la notificacion es para directivos, caso contrario se enviara a tutores")
    private Boolean notifyToAllDirectives;
}
