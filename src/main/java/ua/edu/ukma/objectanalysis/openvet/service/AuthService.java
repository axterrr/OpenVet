package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.dto.auth.LoginRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.security.JwtTokenProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public String register(UserRequest req) {
        LoginRequest login = new LoginRequest(req.getEmail(), req.getPassword());
        userService.create(req);
        return login(login);
    }

    public String login(LoginRequest req) {
        Authentication loginData = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        Authentication auth = authenticationManager.authenticate(loginData);
        String email = auth.getName();
        String role = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        return tokenProvider.generateToken(email, role);
    }
}
