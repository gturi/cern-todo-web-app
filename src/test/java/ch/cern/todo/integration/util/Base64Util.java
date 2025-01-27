package ch.cern.todo.integration.util;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class Base64Util {

    public static String encodeAsBasicAuthHeader(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
