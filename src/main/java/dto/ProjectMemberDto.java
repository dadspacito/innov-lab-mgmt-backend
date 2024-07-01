package dto;

import entity.UserEntity;

import java.time.LocalDateTime;

/**
 * por enquanto enviar dados basicos,
 */
public class ProjectMemberDto {
    int id;
    private String firstName;
    private String lastName;
    private String nickname;

    private LocalDateTime projectEntry;
    //caso seja preciso
    //private LocalDateTime projectLeave;
    private boolean isManager = true;
    private String projectRole = "manager";


    /**
     * tem que ter o role
     * tem de poder fazer set a estar no projeto ou nao. um remove do array do projeto. Método remove user
     * tem de ter role para controlar serviços de projeto. Esse é a maneira de controlar o serviço
     */
    public ProjectMemberDto(){};

    public ProjectMemberDto(int id, String firstName, String lastName, String nickname, LocalDateTime projectEntry) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.projectEntry = projectEntry;
        //this.isManager = isManager;
        //this.projectRole = projectRole;
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

    public LocalDateTime getProjectEntry() {
        return projectEntry;
    }

    public void setProjectEntry(LocalDateTime projectEntry) {
        this.projectEntry = projectEntry;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
}
