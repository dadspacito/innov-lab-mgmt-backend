package enums;

public enum UserType {

    ADMIN("admin"),
    USER("user"),
    GUEST("guest");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
