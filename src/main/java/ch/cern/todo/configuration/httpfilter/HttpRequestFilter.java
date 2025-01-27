package ch.cern.todo.configuration.httpfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class HttpRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        val cachedHttpServletRequest = new CachedHttpServletRequest(request);

        logRequest(cachedHttpServletRequest);

        try {
            filterChain.doFilter(cachedHttpServletRequest, response);
        } catch (IOException | ServletException e) {
            log.error("Error while executing filter chain", e);
            throw e;
        }
    }

    private void logRequest(HttpServletRequest request) throws IOException {
        val stringBuilder = new StringBuilder();

        val method = request.getMethod();
        val path = request.getRequestURL();
        val address = request.getRemoteAddr();
        stringBuilder.append(MessageFormat.format("Request {0} {1} from IP {2}", method, path, address));

        Optional.ofNullable(request.getCookies()).ifPresent(
            cookies -> {
                stringBuilder.append(", REQUEST COOKIES:");
                Arrays.stream(cookies).forEach(
                    cookie -> stringBuilder.append(logKeyValue(cookie.getName(), cookie.getValue())));
            });

        if (request.getHeaderNames().asIterator().hasNext()) {
            stringBuilder.append(", REQUEST HEADERS: ");
            request.getHeaderNames().asIterator().forEachRemaining(name -> {
                if ("authorization".equalsIgnoreCase(name)) {
                    stringBuilder.append(logKeyValue(name, "********"));
                } else {
                    stringBuilder.append(logKeyValue(name, headerValuesToString(request, name)));
                }
            });
        }

        if (request.getParameterNames().asIterator().hasNext()) {
            stringBuilder.append(", REQUEST PARAMS:");
            request.getParameterNames().asIterator().forEachRemaining(
                paramName -> stringBuilder.append(logKeyValue(paramName, request.getParameter(paramName)))
            );
        }

        stringBuilder.append(MessageFormat.format(", REQUEST BODY: {0}",
            IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8))
        );

        log.info("{}", stringBuilder);
    }

    private String headerValuesToString(HttpServletRequest request, String name) {
        return getHeaderValuesAsStream(request, name).collect(Collectors.joining(","));
    }

    private Stream<String> getHeaderValuesAsStream(HttpServletRequest request, String name) {
        val headerValuesIterator = request.getHeaders(name).asIterator();
        val headerValuesSpliterator = Spliterators.spliteratorUnknownSize(headerValuesIterator, Spliterator.ORDERED);
        return StreamSupport.stream(headerValuesSpliterator, false);
    }

    private <T> String logKeyValue(String key, T value) {
        return MessageFormat.format("''{0}'': ''{1}'', ", key, value);
    }
}
