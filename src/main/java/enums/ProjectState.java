package enums;

public enum ProjectState {

    PLANNING,
    READY,
    APPROVED,
    IN_PROGRESS,
    CANCELLED,
    FINISHED;

    public static ProjectState fromString(String state) {
        for (ProjectState s : ProjectState.values()) {
            if (s.name().equalsIgnoreCase(state)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid ProjectState: " + state);
    }
}