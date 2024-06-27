package enums;

public enum TaskState {
    //states
    PLANNED, IN_PROGRESS, FINISHED;
    public  static  TaskState fromString(String state) {
        for (TaskState s : TaskState.values()) {
            if (s.name().equalsIgnoreCase(state)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid SkillType: " + state);
    }


}
