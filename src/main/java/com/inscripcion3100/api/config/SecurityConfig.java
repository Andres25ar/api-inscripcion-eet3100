package com.inscripcion3100.api.config;
import com.inscripcion3100.api.security.AuthEntryPointJwt;
import com.inscripcion3100.api.security.AuthTokenFilter;
import com.inscripcion3100.api.service.impl.UserDetailsService;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

   /* @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //authProvider.setUserDetailsPasswordService();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }*/

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        //rutas Públicas (No requieren token)
                        .requestMatchers("/auth/**").permitAll()

                        //utas exclusivas para usuario administrador
                        .requestMatchers("/users/*/role").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/users/get_all_users").hasAuthority("ADMINISTRADOR")

                        //utas exclusivas para el staff
                        .requestMatchers(HttpMethod.POST, "/courses/create").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.POST, "/courses/clone/**").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.POST, "/notifications/create").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.GET, "/registrations/pending").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.GET, "/registrations/course/**").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.PUT, "/registrations/*/approve").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.PUT, "/registrations/*/reject").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")
                        .requestMatchers(HttpMethod.PUT, "/registrations/*/propose-reassignment").hasAnyAuthority("ADMINISTRADOR", "SECRETARIO")

                        //ruta para conocer staff
                        .requestMatchers(HttpMethod.GET, "/users/staff").hasAnyAuthority("TUTOR", "SECRETARIO", "ADMINISTRADOR")

                        //rutas para tutores y operaciones generales
                        .requestMatchers("/users/me").authenticated()
                        .requestMatchers("/students/**").authenticated()
                        .requestMatchers("/courses/available", "/courses/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/registrations").authenticated() // Crear inscripción
                        .requestMatchers("/registrations/me").authenticated()
                        .requestMatchers("/registrations/student/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/registrations/*/reply-reassignment").authenticated()
                        .requestMatchers("/notifications/unread", "/notifications/unread/count", "/notifications/*/read").authenticated()

                        .requestMatchers("/error").permitAll()

                        //cualquier otra ruta no especificada arriba exige estar autenticado como medida de seguridad extra
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}