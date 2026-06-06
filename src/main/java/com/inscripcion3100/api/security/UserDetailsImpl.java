package com.inscripcion3100.api.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inscripcion3100.api.entity.Role;
import com.inscripcion3100.api.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    //si se agregan campos a la entidad user esto lanzara un error
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userEmail;
    private String userFirstname;
    private String userLastname;
    private Role role;

    @JsonIgnore
    private String password;

    //private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long userId,
            String userEmail,
            String userLastname,
            String userFirstname,
            String password,
            Role role
            //Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userLastname = userLastname;
        this.userFirstname = userFirstname;
        this.password = password;
        this.role = role;
        //this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));    //convertir el enum en GrantedAuthority
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    public Long getUserId(){
        return userId;
    }

    public String getUserFirstname(){
        return userFirstname;
    }

    public String getUserLastname(){
        return userLastname;
    }

    /*
     *  los siguientes metodos deben estar implementados para que siempre un usuario se pueda loggear
     *  sin embargo esto cambia si se agrega a la entidad User campos para verificar si es un usuario habilitado
     */

    //verifica si la cuenta expiró
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //verifica si la cuenta está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //verifica si las credenciales expiraron
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //verifica si la cuenta está habilitada
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     *  build convierte user de la base de datos a user details
     *  toma rol de usuario y los convierte a GrantedAuthority (permisos de spring security)
     *  en este caso uso un solo rol
     */

    public static UserDetailsImpl build (User user){
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserEmail(),
                user.getLastName(),
                user.getFirstName(),
                user.getPassword(),
                user.getRole()
        );
    }
}
