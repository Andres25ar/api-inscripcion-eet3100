package com.inscripcion3100.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    //para registrar eventos y errores
    private final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    // metodo que se ejecuta automanticamente cuando ocurre un error
    //recibe la solicitud del cliente (request), una respuesta (response) y el error de autenticacion
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {
        logger.error("Error de autenticacion, usuario no autorizado: {}", authException.getMessage());

        //define que la respuesta será JSON y no HTML
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //codigo HTTP 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //crea el mensaje de error
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Sin autorizacion");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());     //ruta a la que intentó acceder

        //mapea a JSON
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
