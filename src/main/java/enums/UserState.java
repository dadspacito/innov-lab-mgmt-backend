package enums;

public enum UserState {

    // COOMENTários classe que gere estados de utilizador, se existe, etc etc

    // para erro de dados inválidos
    INVALID_DATA("Invalid data"),
    //para criar um utilizador
    ALREADY_EXISTS("User already exists"),


    CONFIRMED("User is confirmed"),
    NOT_CONFIRMED("User is not confirmed"),
    ACTIVE("User is active"),
    DELETED("User is deleted"),
    UNVERIFIED("User not verified, please confirm your email"),
    NOT_FOUND("User does not exist"),
    VALID("User is valid"),
    // types of user
    ADMIN ("admin"),
    USER ("user"),
    GUEST ("guest"),
    CREATED("User created");



    private final String message;

    UserState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
