package jcrush.system.exceptions;

import java.io.IOException;

public class FileUploadFailedException extends IOException {

    public FileUploadFailedException(String message, Exception cause) {
        super(message, cause);
    }

    public FileUploadFailedException(String message) {
        super(message);
    }
}
