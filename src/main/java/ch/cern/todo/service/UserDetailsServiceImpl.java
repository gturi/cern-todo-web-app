package ch.cern.todo.service;

import ch.cern.todo.configuration.security.CernAuthenticatedUser;
import ch.cern.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }

        val userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            // this will just be logged, a generic message should be exposed to the caller for security reasons
            throw new UsernameNotFoundException("User not found");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(userEntity.getRole().toString());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(authority);

        return new CernAuthenticatedUser(
            username, userEntity.getPassword(), grantedAuthorities,
            userEntity.getUserId(), userEntity.getFirstname(), userEntity.getLastname()
        );
    }
}
