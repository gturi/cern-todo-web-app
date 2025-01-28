package ch.cern.todo.configuration.security;

import ch.cern.todo.model.api.ErrorResponseApi;
import ch.cern.todo.util.ObjectMapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("AuthenticationEntryPoint: unauthorized request", authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            val responseBody = new ErrorResponseApi("Unauthorized");
            val jsonResponseBody = ObjectMapperUtil.asJsonString(responseBody);
            response.getWriter().write(jsonResponseBody);
        } catch (RuntimeException e) {
            // it should never happen, but just in case we return an empty JSON object if it ever does
            log.error("Failed to write Unauthorized error response, returning empty JSON object", e);
            response.getWriter().write("{}"); // Empty JSON object
        }
    }
}
