package enums;

public enum MaterialType {
    COMPONENT,
    RESOURCE;
    // Method to convert a string to a MaterialType enum
    public static MaterialType fromString(String type) {
        for (MaterialType m : MaterialType.values()) {
            if (m.name().equalsIgnoreCase(type)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid MaterialType: " + type);
    }
}
