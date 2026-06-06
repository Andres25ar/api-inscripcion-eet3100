package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.user.UserProfileDTO;

import java.util.List;

public interface IUserService {
    List<UserProfileDTO> getAllUsers();
}
