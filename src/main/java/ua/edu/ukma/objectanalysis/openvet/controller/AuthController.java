package ua.edu.ukma.objectanalysis.openvet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.objectanalysis.openvet.dto.auth.LoginRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.auth.LoginResponse;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.service.AuthService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<LoginResponse> registerOwner(@RequestBody UserRequest request) {
        String token = authService.register(request);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }
}
