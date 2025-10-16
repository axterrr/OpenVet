package ua.edu.ukma.objectanalysis.openvet.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public String getAuthenticatedUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
