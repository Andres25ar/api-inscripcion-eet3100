package com.inscripcion3100.api.mapper;

import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.auth.AuthResponseDTO;
import com.inscripcion3100.api.dto.auth.RegisterRequestDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.dto.user.UserProfileDTO;
import com.inscripcion3100.api.entity.Role;
import com.inscripcion3100.api.entity.Student;
import com.inscripcion3100.api.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserMapper {
    public static UserProfileDTO toUserProfileDTO (User user){
        if (user == null){
            return null;
        }

        List<StudentResponseDTO> students = new ArrayList<>();

        //verificamos que la colección no sea null
        if (user.getStudentsInCharge() != null) {
            students = user.getStudentsInCharge().stream()
                    // 3. Delegamos la conversión a un StudentMapper (que deberás crear)
                    .map(student -> StudentMapper.toStudentResponseDTO(student))
                    // 4. Recolectamos el resultado en una nueva lista
                    .collect(Collectors.toList());
        }
        return new UserProfileDTO(
                user.getUserId(),
                user.getUserEmail(),
                user.getDni(),
                user.getCuil(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserPhone(),
                user.getUserAddress(),
                user.getDateOfBirth(),
                students,
                user.getRole()
        );
    }

    public static StaffResponseDTO toStaffDto(User user){
        if (user == null || user.getRole() == Role.TUTOR){
            return null;
        }
        return new StaffResponseDTO(
                user.getUserId(),
                user.getDni(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getRole()
        );
    }

    public static User toUserEntity(RegisterRequestDTO dto, String encryptedPassword) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setDni(dto.getDni());
        user.setCuil(dto.getCuil());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUserEmail(dto.getEmail());
        user.setPassword(encryptedPassword);
        user.setUserPhone(dto.getUserPhone());
        user.setUserAddress(dto.getUserAddress());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setOcupation(dto.getOcupation());
        user.setRelationship(dto.getRelationship());
        user.setRole(Role.TUTOR);

        return user;
    }
}
