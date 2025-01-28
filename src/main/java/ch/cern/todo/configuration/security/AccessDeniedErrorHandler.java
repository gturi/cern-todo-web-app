package ch.cern.todo.configuration.security;

import ch.cern.todo.model.api.ErrorResponseApi;
import ch.cern.todo.util.ObjectMapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedErrorHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("Access denied handler", accessDeniedException);

        // it is often better to avoid differentiating between 403 and 401, to give potential attackers less information
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
