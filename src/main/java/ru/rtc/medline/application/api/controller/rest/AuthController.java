package ru.rtc.medline.application.api.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rtc.medline.application.api.dto.RequestLoginDto;
import ru.rtc.medline.application.api.dto.ResponseTokenDto;
import ru.rtc.medline.application.infrastructure.common.configuration.CustomUserDetailsService;
import ru.rtc.medline.application.infrastructure.common.configuration.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto dto) {

        Authentication authRequest =
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword());

        try {
            authenticationManager.authenticate(authRequest);

            UserDetails user = userDetailsService.loadUserByUsername(dto.getLogin());
            String token = jwtService.generateToken(user);

            ResponseTokenDto res = new ResponseTokenDto();
            res.setToken(token);

            return ResponseEntity.ok(res);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
