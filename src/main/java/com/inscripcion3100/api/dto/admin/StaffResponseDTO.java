package com.inscripcion3100.api.dto.admin;

import com.inscripcion3100.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponseDTO {
    private Long dni;
    private String firstName;
    private String lastName;
    private Role role;
}
