package entity;

import java.util.List;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="User")
@NamedQueries(
        {
                @NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
                @NamedQuery(name = "User.findUserById", query = "SELECT u FROM UserEntity u WHERE u.id = :id"),
                @NamedQuery(name = "User.findAllUsers", query = "SELECT u FROM UserEntity u"),
                @NamedQuery(name = "User.findUserByEmailToken", query = "SELECT u FROM UserEntity u WHERE u.emailToken = :emailToken"),
                @NamedQuery(name = "User.findUserByNickname", query = "SELECT u FROM UserEntity u WHERE u.nickname = :nickname")
        }

)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "firstName", nullable = false, unique = false, updatable = true)
    private String firstName;

    @Column(name = "lastName", nullable = false, unique = false, updatable = true)
    private String lastName;

    @Column(name = "nickname", nullable = true, unique = true, updatable = true)
    private String nickname;


    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "password", nullable = false, unique = false, updatable = true)
    private String password;

    @Column(name = "avatar", nullable = false, unique = false, updatable = true)
    private String avatar;

    @Column(name = "bio", nullable = true, unique = false, updatable = true)
    private String bio;

    @Column(name = "createdAt", nullable = false, unique = false, updatable = true)
    private LocalDateTime createdAt;

    @Column(name = "isAdmin", nullable = false, unique = false, updatable = true)
    private boolean isAdmin;

    @Column(name = "isPublicProfile", nullable = false, unique = false, updatable = true)
    private boolean isPublicProfile;

    @Column(name = "isConfirmed", nullable = false, unique = false, updatable = true)
    private boolean isConfirmed;

    @Column(name = "isDeleted", nullable = false, unique = false, updatable = true)
    private boolean isDeleted;

    @Column(name = "emailToken", nullable = true, unique = false, updatable = true)
    private String emailToken;

    @Column(name = "emailTokenExpires", nullable = true, unique = false, updatable = true)
    private LocalDateTime emailTokenExpires;

    @ManyToOne
    @JoinColumn(name = "workplace_id", nullable = false)
    private WorkplaceEntity workplace;

    //interesses, skills, tabela tokens, projetos.




    public UserEntity() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isPublicProfile() {
        return isPublicProfile;
    }

    public void setPublicProfile(boolean publicProfile) {
        isPublicProfile = publicProfile;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }

    public LocalDateTime getEmailTokenExpires() {
        return emailTokenExpires;
    }

    public void setEmailTokenExpires(LocalDateTime emailTokenExpires) {
        this.emailTokenExpires = emailTokenExpires;
    }

    public WorkplaceEntity getWorkplace() {
        return workplace;
    }

    public void  setWorkplace(WorkplaceEntity workplace) {
        this.workplace = workplace;
    }
}
