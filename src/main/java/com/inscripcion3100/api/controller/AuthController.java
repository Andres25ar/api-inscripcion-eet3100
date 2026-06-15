package com.inscripcion3100.api.controller;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.auth.AuthResponseDTO;
import com.inscripcion3100.api.dto.auth.LoginRequestDTO;
import com.inscripcion3100.api.dto.auth.RegisterRequestDTO;
import com.inscripcion3100.api.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser (
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
            ){
        MessageResponse response = authService.register(registerRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
            ){
        AuthResponseDTO response = authService.login(loginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
