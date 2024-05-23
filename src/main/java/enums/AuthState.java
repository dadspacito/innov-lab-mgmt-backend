package enums;


public enum AuthState {
    ACTIVE("Token is active"),
    EXPIRED("Token has expired, please log in again"),
    MISSING("Token is missing, please log in"),
    UNVERIFIED("User not verified, please confirm your email"),
    USER_NOT_FOUND("User does not exist"),
    UNAUTHORIZED("User does not have permissions");

    private final String message;

    AuthState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
