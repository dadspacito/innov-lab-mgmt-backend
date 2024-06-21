package dto;


import dao.SessionTokenDao;
import entity.UserEntity;
import entity.WorkplaceEntity;
import jakarta.ejb.EJB;



public class PostLoginDto {


    private int id;
    private String token;
    private int timeout;
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String avatar;
    private String bio;
    private int workplaceId;




    // adicionar foto e nome para header?

    /**
     * username
     * credenciais para a página de utlizador
     *
     */
    public PostLoginDto() {
    }

    //este construtor pode levar outros detalhes
    public PostLoginDto(UserEntity ue, String token, int timeout) {
        this.id = ue.getId();
        this.token = token;
        this.timeout = timeout;
        this.firstName = ue.getFirstName();
        this.lastName = ue.getLastName();
        this.nickname = ue.getNickname();
        this.email = ue.getEmail();
        this.avatar = ue.getAvatar();
        this.bio = ue.getBio();
        this.workplaceId = ue.getWorkplace().getId();

    }

    // getters and setters all
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(int workplace) {
        this.workplaceId = workplace;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
