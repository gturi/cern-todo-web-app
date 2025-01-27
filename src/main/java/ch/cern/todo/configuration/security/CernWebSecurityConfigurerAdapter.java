package ch.cern.todo.configuration.security;

import ch.cern.todo.model.business.Role;
import ch.cern.todo.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CernWebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(expressionInterceptUrlRegistry ->
                expressionInterceptUrlRegistry
                    // instead of having a shared path for all roles,
                    // the application can be organized to provide different paths for different roles
                    .requestMatchers(new AntPathRequestMatcher("/v1/admin/**")).hasAnyAuthority(Role.ROLE_ADMIN.name())
                    .requestMatchers(new AntPathRequestMatcher("/v1/user/**")).hasAnyAuthority(Role.ROLE_USER.name())
                    .anyRequest().authenticated()
            )
            .httpBasic(httpSecurityHttpBasicConfigurer ->
                httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint)
            )
            .userDetailsService(userDetailsService);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // H2 console has its own login form, so it should be ignored
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }
}
