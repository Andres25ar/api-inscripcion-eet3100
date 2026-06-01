package com.inscripcion3100.api.dto.admin;

import com.inscripcion3100.api.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequestDTO {
    @NotNull(message = "Es necesario especificar el usuario a asignar nuevo rol")
    private Long userId;

    @NotNull(message = "Es necesario especificar el nuevo rol")
    private Role newRole;
}
