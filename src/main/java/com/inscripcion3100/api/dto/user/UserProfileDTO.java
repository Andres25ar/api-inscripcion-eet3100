package com.inscripcion3100.api.dto.user;

import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long userId;
    private String userEmail;       //email del usuario
    private Long dni;               //dni de usuario
    private String cuil;            //cuil del usuario
    private String firstName;       //nombre del usuario
    private String lastName;        //aperllido del usuario
    private String userPhone;       //numero de telefono
    private String userAddress;     //direccion
    private LocalDate dateOfBirth;  //fecha de nacimiento
    private List<StudentResponseDTO> studentsInCharge = new ArrayList<>(); //alumnos a cargo, guarda el id
    private Role role;
}
