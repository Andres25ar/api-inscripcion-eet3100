package com.inscripcion3100.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;   //jason web token para autenticacion
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
