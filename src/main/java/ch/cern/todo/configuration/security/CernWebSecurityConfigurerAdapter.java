package ch.cern.todo.configuration.security;

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
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(expressionInterceptUrlRegistry ->
                expressionInterceptUrlRegistry.anyRequest().authenticated()
            )
            .httpBasic(httpSecurityHttpBasicConfigurer ->
                httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint)
            )
            .userDetailsService(userDetailsService);
        //http.addFilterAfter(/*new CustomFilter(),*/ BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // H2 console has its own login form, so it should be ignored
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }
}
