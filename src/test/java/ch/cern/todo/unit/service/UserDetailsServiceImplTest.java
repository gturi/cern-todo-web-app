package ch.cern.todo.unit.service;

import ch.cern.todo.configuration.security.CernAuthenticatedUser;
import ch.cern.todo.model.business.Role;
import ch.cern.todo.model.database.UserEntity;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.service.UserDetailsServiceImpl;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setUsername("testUser");
        userEntity.setPassword(new BCryptPasswordEncoder().encode("testPassword"));
        userEntity.setRole(Role.ROLE_USER);
        userEntity.setFirstname("Test");
        userEntity.setLastname("User");
    }

    @Test
    void loadUserByUsername_ValidUsername_ShouldReturnUserDetails() {
        when(userRepository.findByUsername("testUser")).thenReturn(userEntity);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertNotEquals("testPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals(Role.ROLE_USER.name())));
        assertInstanceOf(CernAuthenticatedUser.class, userDetails);

        val cernAuthenticatedUser = (CernAuthenticatedUser) userDetails;
        assertEquals(1L, cernAuthenticatedUser.getUserId());
        assertEquals("Test", cernAuthenticatedUser.getFirstname());
        assertEquals("User", cernAuthenticatedUser.getLastname());

        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void loadUserByUsername_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername("unknownUser"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }

    @Test
    void loadUserByUsername_EmptyUsername_ShouldThrowException() {
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername(""));

        assertEquals("Username is empty", exception.getMessage());
        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    void loadUserByUsername_NullUsername_ShouldThrowException() {
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername(null));

        assertEquals("Username is empty", exception.getMessage());
        verify(userRepository, never()).findByUsername(anyString());
    }
}
