package enums;

public enum SkillType {
    KNOWLEDGE,
    SOFTWARE,
    HARDWARE,
    TOOLS;

    public  static  SkillType fromString(String type) {
        for (SkillType s : SkillType.values()) {
            if (s.name().equalsIgnoreCase(type)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid SkillType: " + type);
    }

}
