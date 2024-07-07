package dto;

import entity.UserEntity;

import java.time.LocalDateTime;

/**
 * por enquanto enviar dados basicos,
 */
public class ProjectMemberDto {
    int id;
    private String name;
    ;
    private String nickname;

    private LocalDateTime projectEntry;
    //caso seja preciso
    //private LocalDateTime projectLeave;
    private String projectRole;


    /**
     * tem que ter o role
     * tem de poder fazer set a estar no projeto ou nao. um remove do array do projeto. Método remove user
     * tem de ter role para controlar serviços de projeto. Esse é a maneira de controlar o serviço
     */
    public ProjectMemberDto(){};

    public ProjectMemberDto(int id, String firstName, String lastName, String nickname, LocalDateTime projectEntry) {
        this.id = id;
        this.name = firstName + " " + lastName;
        this.nickname = nickname;
        this.projectEntry = projectEntry;
        this.projectRole = projectRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
}
