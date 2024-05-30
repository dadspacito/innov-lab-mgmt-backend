package exception;

// Custom exception class
public class TokenCreationException extends RuntimeException {
    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
