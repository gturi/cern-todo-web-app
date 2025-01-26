package ch.cern.todo.model.business;

import java.util.Set;

public record LoggedUserInfo(Long userId,
                             String username,
                             String firstname,
                             String lastname,
                             // right now for simplicity the application only supports one role per user,
                             // but it is best to foresee the support for multiple roles
                             Set<Role> roles) {

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }
}
