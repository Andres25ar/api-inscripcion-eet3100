package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.auth.AuthResponseDTO;
import com.inscripcion3100.api.dto.auth.LoginRequestDTO;
import com.inscripcion3100.api.dto.auth.RegisterRequestDTO;

public interface IAuthService {
    MessageResponse register(RegisterRequestDTO requestDTO);
    AuthResponseDTO login(LoginRequestDTO requestDTO);
}
