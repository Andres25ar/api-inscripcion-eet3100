package com.inscripcion3100.api.security;

import com.inscripcion3100.api.service.impl.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;    //borrar
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                //obtiene email por medio del token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                //obtiene datos del usuario de la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //crea un token de autenticacion con datos del usuario
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);   //guardar en contexto de spring
            }
        }
        catch (Exception e){
            logger.error("No se pudo autenticar usuario: {}", e.getMessage());
        }

        //peticion para el prox filtro o controller
        filterChain.doFilter(request, response);
    }

    private String parseJwt (HttpServletRequest request){
        //obtener cabecera de "Authorization"
        String headerAuth = request.getHeader("Authorization");

        //debe existir y debe empezar con "Bearer "
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);     //devuelve token sin "Bearer "
        }
        return null;
    }
}
