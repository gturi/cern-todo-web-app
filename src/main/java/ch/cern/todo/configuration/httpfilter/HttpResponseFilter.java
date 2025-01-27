package ch.cern.todo.configuration.httpfilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.StringJoiner;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpResponseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(servletRequest, cachingResponseWrapper);
        } catch (IOException | ServletException e) {
            log.error("Error while executing filter chain", e);
            throw e;
        } finally {
            val stringBuilder = new StringJoiner(", ");

            val status = cachingResponseWrapper.getStatus();
            stringBuilder.add(MessageFormat.format("RESPONSE STATUS: {0}", status));

            val headers = new StringJoiner(" | ");
            cachingResponseWrapper.getHeaderNames().forEach(
                name -> headers.add(logKeyValue(name, String.join(",", cachingResponseWrapper.getHeaders(name))))
            );
            stringBuilder.add("RESPONSE HEADERS: " + headers);

            byte[] responseContent = cachingResponseWrapper.getContentAsByteArray();

            String body;
            if (ArrayUtils.isEmpty(responseContent)) {
                body = "is empty";
            } else if (MimeTypeUtils.APPLICATION_JSON_VALUE.equalsIgnoreCase(response.getContentType())
                || MimeTypeUtils.TEXT_PLAIN_VALUE.equalsIgnoreCase(response.getContentType())) {
                body = new String(responseContent, StandardCharsets.UTF_8);
            } else {
                body = "not logged since it is not json or text";
            }
            stringBuilder.add(MessageFormat.format(" RESPONSE BODY: {0}", body));

            response.getOutputStream().write(responseContent);

            log.info("{}", stringBuilder);
        }
    }

    private <T> String logKeyValue(String key, T value) {
        return MessageFormat.format("''{0}'': ''{1}''", key, value);
    }
}
