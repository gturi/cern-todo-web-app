package ch.cern.todo.util;

import ch.cern.todo.configuration.security.CernAuthenticatedUser;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.Role;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class LoggedUserUtils {

    public static LoggedUserInfo getLoggedUserInfo() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(principal -> {
                if (principal instanceof CernAuthenticatedUser user) {
                    return user;
                }
                return null;
            }).map(cernAuthenticatedUser -> new LoggedUserInfo(
                cernAuthenticatedUser.getUserId(), cernAuthenticatedUser.getUsername(),
                cernAuthenticatedUser.getFirstname(), cernAuthenticatedUser.getLastname(),
                authoritiesToRoles(cernAuthenticatedUser.getAuthorities())
            ))
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private static Set<Role> authoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return CollectionUtils.emptyIfNull(authorities).stream()
            .map(GrantedAuthority::getAuthority)
            .map(Role::valueOf)
            .collect(Collectors.toUnmodifiableSet());
    }
}
