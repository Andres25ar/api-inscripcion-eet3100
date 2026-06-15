package com.inscripcion3100.api.controller;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.user.ChangeRoleRequestDTO;
import com.inscripcion3100.api.dto.user.UserProfileDTO;
import com.inscripcion3100.api.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
//@PreAuthorize()
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyPerfil(Authentication auth){
        String userEmail = auth.getName();
        UserProfileDTO profile = userService.getMyProfile(userEmail);
        return ResponseEntity.ok(profile);
    }

    /*@PutMapping("/me")
    public ResponseEntity<MessageResponse> updateMyProfile(
            @Valid @RequestBody UserProfileDTO ?? request,
            Authentication auth
    ){
        String userEmail = auth.getName();
        MessageResponse response = userService.updateMyProfile(userEmail, request); --falta implementar
        return ResponseEntity.ok(response);
    }*/

    @GetMapping("/staff")
    @PreAuthorize("hasAnyAuthority('TUTOR', 'SECRETARIO')")
    public ResponseEntity<List<StaffResponseDTO>> getStaff (){
        List<StaffResponseDTO> staff = userService.getStaffMembers();
        return ResponseEntity.ok(staff);
    }

    //PUT /api/users/{userId}/role
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<MessageResponse> changeRole (
            @PathVariable("userId") Long userId,
            @Valid @RequestBody ChangeRoleRequestDTO request){
        MessageResponse messageResponse = userService.changeUserRole(userId, request.getNewRole());
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/get_all_users")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers (){
        List<UserProfileDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
