package enums;


public enum AuthState {

    ACTIVE("Token is active"),
    INVALID("Token is invalid, please log in again"),
    EXPIRED("Token has expired, please log in again"),
    MISSING("Token is missing, please log in"),
    UNAUTHORIZED("User does not have permissions");

    private final String message;

    AuthState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
