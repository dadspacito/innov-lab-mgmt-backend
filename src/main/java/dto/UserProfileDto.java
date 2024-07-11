package dto;

import java.util.Set;

public class UserProfileDto {
    // dto semelhante apenas com os campos que são necessários para o perfil público do utilizador
    // juntar as skills e interesses para visualização?
    // na edição buscar as skills e interesses
    //tem de ir buscar também a workplace

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String nickname;
    private String avatar;
    private String bio;
    private boolean isPublicProfile;
    private WorkplaceDto workplace;
    private Set<BasicProjectDto> isInProject;
    private Set<SkillDto> skills;
    private Set<InterestDto> interests;

    public UserProfileDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isPublicProfile() {
        return isPublicProfile;
    }

    public void setPublicProfile(boolean publicProfile) {
        isPublicProfile = publicProfile;
    }

    public WorkplaceDto getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceDto workplace) {
        this.workplace = workplace;
    }

    public Set<BasicProjectDto> getIsInProject() {
        return isInProject;
    }

    public void setIsInProject(Set<BasicProjectDto> isInProject) {
        this.isInProject = isInProject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillDto> skills) {
        this.skills = skills;
    }

    public Set<InterestDto> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestDto> interests) {
        this.interests = interests;
    }
}
