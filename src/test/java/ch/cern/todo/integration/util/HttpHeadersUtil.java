package ch.cern.todo.integration.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class HttpHeadersUtil {

    public static HttpHeaders getAdminHeaders() {
        return getAuthorizationHeaders("cernAdmin", "CernAdminPassword");
    }

    public static HttpHeaders getAliceHeaders() {
        return getAuthorizationHeaders("aliceUser", "AliceUserPassword");
    }

    public static HttpHeaders getAuthorizationHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, Base64Util.encodeAsBasicAuthHeader(username, password));
        return headers;
    }
}
