package ua.edu.ukma.objectanalysis.openvet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.auth.LoginRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.security.JwtTokenProvider;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private UserRequest userRequest;
    private LoginRequest loginRequest;
    private UserEntity testUser;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .email("test@test.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phone("+380123456789")
                .role(UserRole.PET_OWNER)
                .build();

        loginRequest = new LoginRequest("test@test.com", "password123");

        testUser = new PetOwnerEntity();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");

        authentication = mock(Authentication.class);
    }

    // ===== REGISTER Tests =====

    @Test
    @DisplayName("Should register user successfully and return token")
    void testRegister_Success() {
        // Arrange
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("PET_OWNER"));

        when(userService.create(any(UserRequest.class))).thenReturn(testUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@test.com");
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        when(tokenProvider.generateToken(anyString(), anyString())).thenReturn("mock-jwt-token");

        // Act
        String token = authService.register(userRequest);

        // Assert
        assertNotNull(token);
        assertEquals("mock-jwt-token", token);
        verify(userService).create(userRequest);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken("test@test.com", "PET_OWNER");
    }

    @Test
    @DisplayName("Should throw exception when user creation fails during registration")
    void testRegister_UserCreationFails() {
        // Arrange
        when(userService.create(any(UserRequest.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(userRequest));
        verify(userService).create(userRequest);
        verify(authenticationManager, never()).authenticate(any());
        verify(tokenProvider, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw exception when authentication fails during registration")
    void testRegister_AuthenticationFails() {
        // Arrange
        when(userService.create(any(UserRequest.class))).thenReturn(testUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.register(userRequest));
        verify(userService).create(userRequest);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(anyString(), anyString());
    }

    // ===== LOGIN Tests =====

    @Test
    @DisplayName("Should login successfully and return token")
    void testLogin_Success() {
        // Arrange
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("PET_OWNER"));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@test.com");
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        when(tokenProvider.generateToken(anyString(), anyString())).thenReturn("mock-jwt-token");

        // Act
        String token = authService.login(loginRequest);

        // Assert
        assertNotNull(token);
        assertEquals("mock-jwt-token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken("test@test.com", "PET_OWNER");
    }

    @Test
    @DisplayName("Should throw exception when login credentials are invalid")
    void testLogin_InvalidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Should handle empty authority list gracefully")
    void testLogin_NoAuthorities() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@test.com");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(tokenProvider.generateToken(anyString(), isNull())).thenReturn("mock-jwt-token");

        // Act
        String token = authService.login(loginRequest);

        // Assert
        assertNotNull(token);
        verify(tokenProvider).generateToken("test@test.com", null);
    }

    @Test
    @DisplayName("Should extract role from multiple authorities")
    void testLogin_MultipleAuthorities() {
        // Arrange
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ADMIN"));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin@test.com");
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        when(tokenProvider.generateToken(anyString(), anyString())).thenReturn("admin-jwt-token");

        // Act
        String token = authService.login(new LoginRequest("admin@test.com", "adminpass"));

        // Assert
        assertNotNull(token);
        assertEquals("admin-jwt-token", token);
        verify(tokenProvider).generateToken("admin@test.com", "ADMIN");
    }
}
