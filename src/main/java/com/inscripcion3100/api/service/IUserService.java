package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.user.UserProfileDTO;
import com.inscripcion3100.api.entity.Role;

import java.util.List;

public interface IUserService {
    List<UserProfileDTO> getAllUsers();

    UserProfileDTO getMyProfile(String userEmail);

    List<StaffResponseDTO> getStaffMembers();

    MessageResponse changeUserRole(Long userId, Role newRole);

    //UserProfileDTO updateMyProfile (String email, UserUpdateRequestDTO);
}
