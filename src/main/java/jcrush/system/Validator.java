package jcrush.system;

import java.io.IOException;
import java.security.InvalidParameterException;

public class Validator {

    public static void validateNotNull(Object value, String name) throws InvalidParameterException {
        if (value == null)
            throw new InvalidParameterException("The parameter \"" + value + "\" cannot be null!");
    }

    public static void validateNot404(String json) throws IOException {
        if (json.contains("404"))
            throw new IOException("The server returned 404!");
    }
}
