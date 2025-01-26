package ch.cern.todo.configuration.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CernAuthenticatedUser extends User {

    private final Long userId;
    private final String firstname;
    private final String lastname;

    public CernAuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
                                 Long userId, String firstname, String lastname) {
        super(username, password, authorities);
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
