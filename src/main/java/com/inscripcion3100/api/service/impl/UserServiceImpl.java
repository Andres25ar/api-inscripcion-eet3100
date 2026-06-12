package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.user.UserProfileDTO;
import com.inscripcion3100.api.entity.Role;
import com.inscripcion3100.api.entity.User;
import com.inscripcion3100.api.exception.ResourceNotFoundException;
import com.inscripcion3100.api.mapper.UserMapper;
import com.inscripcion3100.api.repository.UserRepository;
import com.inscripcion3100.api.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    public UserServiceImpl (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper :: toUserProfileDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getMyProfile(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return UserMapper.toUserProfileDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDTO> getStaffMembers() {
        List<User> userStaff = userRepository.findByRole(Role.SECRETARIO);
        //userStaff.add(userRepository.findByRole(Role.ADMINISTRADOR));
        userStaff.addAll(userRepository.findByRole(Role.ADMINISTRADOR));
        return userStaff.stream()
                .map(UserMapper:: toStaffDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse changeUserRole(Long userId, Role newRole) {
        if(newRole == null){
            return new MessageResponse("Especifi que nuevo rol");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getRole() == newRole) {
            throw new IllegalArgumentException("Este usuario ya es un administrativo de la institucion");
        }

        user.setRole(newRole);

        userRepository.save(user);

        return new MessageResponse("El usuario " + user.getFirstName() + " ha sido ascendido a  " + newRole.toString());
    }
}
