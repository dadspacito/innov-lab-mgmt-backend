package enums;

public enum ProjectState {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public static ProjectState fromString(String state) {
        for (ProjectState s : ProjectState.values()) {
            if (s.name().equalsIgnoreCase(state)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid ProjectState: " + state);
    }
}