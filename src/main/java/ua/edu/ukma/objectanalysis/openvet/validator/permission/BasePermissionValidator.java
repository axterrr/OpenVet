package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.exception.ForbiddenException;

public abstract class BasePermissionValidator<ENTITY> {

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    protected boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    protected Long getAuthenticatedUserId() {
        if (!isAuthenticated()) { return null; }
        return (Long) getAuthentication().getPrincipal();
    }

    protected UserRole getAuthenticatedUserRole() {
        if (!isAuthenticated()) { return null; }
        String roleString = getAuthentication().getAuthorities()
            .stream()
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .getAuthority();
        return UserRole.valueOf(roleString);
    }

    protected void forbid() {
        throw new ForbiddenException();
    }

    protected void require(boolean condition) {
        if (!condition) {
            forbid();
        }
    }

    protected void requireUserRole(UserRole role) {
        require(getAuthenticatedUserRole() == role);
    }

    protected void requireUserId(Long id) {
        require(getAuthenticatedUserId().equals(id));
    }

    protected boolean isAuthenticatedUserAdmin() {
        return getAuthenticatedUserRole() == UserRole.ADMIN;
    }

    protected boolean isAuthenticatedUserVeterinarian() {
        return getAuthenticatedUserRole() == UserRole.VETERINARIAN;
    }

    protected boolean isAuthenticatedUserPetOwner() {
        return getAuthenticatedUserRole() == UserRole.PET_OWNER;
    }

    public abstract void validateForGetAll();
    public abstract void validateForGet(ENTITY entity);
    public abstract void validateForCreate();
    public abstract void validateForUpdate(ENTITY entity);
    public abstract void validateForDelete(ENTITY entity);
}
