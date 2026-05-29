package com.inscripcion3100.api.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class StudenteResponseDTO {
    private Long studentDni;
    private String studentCuil;
    private String lastName;
    private String firstName;
    private String studentEmail;
    private String studentPhone;
    private Date birthdate;
    private String birthplace;
    private String address;
}
