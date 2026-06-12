package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.auth.AuthResponseDTO;
import com.inscripcion3100.api.dto.auth.LoginRequestDTO;
import com.inscripcion3100.api.dto.auth.RegisterRequestDTO;
import com.inscripcion3100.api.entity.Role;
import com.inscripcion3100.api.entity.User;
import com.inscripcion3100.api.repository.UserRepository;
import com.inscripcion3100.api.security.JwtUtils;
import com.inscripcion3100.api.security.UserDetailsImpl;
import com.inscripcion3100.api.service.IAuthService;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public MessageResponse register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUserEmail(requestDTO.getEmail())){
            throw new IllegalArgumentException("ERROR: Este email ya se encuentra en uso");
        }
        /*if (userRepository.existsByDni(requestDTO.getDni())){
            throw new IllegalArgumentException("ERROR: Este DNI ya pertenece a un usuario registrado");
        }*/
        if (userRepository.existsByCuil(requestDTO.getCuil())){
            throw new IllegalArgumentException("ERROR: Este cuil ya pertenece a un usuario registrado");
        }

        User user = new User();

        user.setUserEmail(requestDTO.getEmail());
        user.setCuil(requestDTO.getCuil());
        user.setDni(requestDTO.getDni());
        user.setLastName(requestDTO.getLastName());
        user.setFirstName(requestDTO.getFirstName());
        user.setUserPhone(requestDTO.getUserPhone());
        user.setUserAddress(requestDTO.getUserAddress());
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        user.setOcupation(requestDTO.getOcupation());
        user.setRelationship(requestDTO.getRelationship());
        user.setStudiesAchieved(requestDTO.getStudiesAchieved());

        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        //por defecto el rol sera de tutor hasta que un admin lo promueva
        user.setRole(Role.TUTOR);

        userRepository.save(user);

        return new MessageResponse("USUARIO REGISTRADO CORRECTAMENTE");
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        //comprueba verificacion con UserDetailsServiceImpl y PasswordEncoder con email o cuil y contraseña de la request
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getEmailOrCuil(), requestDTO.getPassword())
        );

        //para guardar autenticacion en el contexto (host)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new AuthResponseDTO(
                jwt,
                userDetails.getUsername(),
                userDetails.getUserFirstname(),
                userDetails.getUserLastname(),
                roles
        );
    }
}
