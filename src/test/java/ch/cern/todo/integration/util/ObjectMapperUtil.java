package ch.cern.todo.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperUtil {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readJson(@NotNull final String jsonString,  Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
