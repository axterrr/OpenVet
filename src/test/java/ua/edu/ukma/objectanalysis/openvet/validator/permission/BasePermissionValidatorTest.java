package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasePermissionValidatorTest {

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ConcretePermissionValidator basePermissionValidator;

    private static class ConcretePermissionValidator extends BasePermissionValidator<Object, Object> {
        @Override
        public void validateForGetAll() {}
        @Override
        public void validateForGet(Object entity) {}
        @Override
        public void validateForCreate(Object request) {}
        @Override
        public void validateForUpdate(Object entity) {}
        @Override
        public void validateForDelete(Object entity) {}
    }

    @BeforeEach
    void setUp() {
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void isAuthenticated_AnonymousUser_ReturnsFalse() {
        AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken("key", "anonymousUser",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        when(securityContext.getAuthentication()).thenReturn(anonymousToken);
        assertFalse(basePermissionValidator.isAuthenticated());
    }

    @Test
    void getAuthenticatedUserEmail_AuthenticatedUser_ReturnsEmail() {
        when(authentication.getPrincipal()).thenReturn("user@example.com");
        lenient().when(authentication.getAuthorities()).thenReturn((Collection) Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        assertEquals("user@example.com", basePermissionValidator.getAuthenticatedUserEmail());
    }

    @Test
    void getAuthenticatedUserRole_Admin_ReturnsAdminRole() {
        lenient().when(authentication.getPrincipal()).thenReturn("user");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        lenient().when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        assertEquals(UserRole.ADMIN, basePermissionValidator.getAuthenticatedUserRole());
    }

    @Test
    void forbid_ThrowsForbiddenException() {
        assertThrows(ForbiddenException.class, () -> basePermissionValidator.forbid());
    }

    @Test
    void require_TrueCondition_NoException() {
        assertDoesNotThrow(() -> basePermissionValidator.require(true));
    }

    @Test
    void require_FalseCondition_ThrowsForbiddenException() {
        assertThrows(ForbiddenException.class, () -> basePermissionValidator.require(false));
    }
}
