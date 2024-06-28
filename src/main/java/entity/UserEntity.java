package entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="User")
@NamedQueries({
        @NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
        @NamedQuery(name = "User.findUserById", query = "SELECT u FROM UserEntity u WHERE u.id = :id"),
        @NamedQuery(name = "User.findAllUsers", query = "SELECT u FROM UserEntity u"),
        @NamedQuery(name = "User.findUserByEmailToken", query = "SELECT u FROM UserEntity u WHERE u.emailToken = :emailToken"),
        @NamedQuery(name = "User.findUserByNickname", query = "SELECT u FROM UserEntity u WHERE u.nickname = :nickname")
})
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "nickname", nullable = true, unique = true)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "bio", nullable = true)
    private String bio;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "isAdmin", nullable = false)
    private boolean isAdmin;

    @Column(name = "isPublicProfile", nullable = false)
    private boolean isPublicProfile;

    @Column(name = "isConfirmed", nullable = false)
    private boolean isConfirmed;

    @Column(name = "isActive", nullable = false)
    private boolean isActive = true;

    @Column(name = "emailToken", nullable = true)
    private String emailToken;

    @Column(name = "emailTokenExpires", nullable = true)
    private LocalDateTime emailTokenExpires;

    @ManyToOne
    @JoinColumn(name = "workplace_id", nullable = false)
    private WorkplaceEntity workplace;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_skill",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillEntity> skills = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<InterestEntity> interests = new HashSet<>();




    public UserEntity() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public void setWorkplace(WorkplaceEntity workplace) {
        this.workplace = workplace;
    }

    public Set<SkillEntity> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillEntity> skills) {
        this.skills = skills;
    }

    public Set<InterestEntity> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestEntity> interests) {
        this.interests = interests;
    }

    // MÉTODOS PARA AS LISTAS
    // Método para adicionar uma skill ao user
    public void addSkill(SkillEntity skill) {
        this.skills.add(skill);
        skill.getUsers().add(this);
    }

    // Método para remover uma skill do user
    public void removeSkill(SkillEntity skill) {
        this.skills.remove(skill);
        skill.getUsers().remove(this);
    }

    // Método para adicionar um interesse ao user
    public void addInterest(InterestEntity interest) {
        this.interests.add(interest);
        interest.getUsers().add(this);
    }

    // Método para remover um interesse do user
    public void removeInterest(InterestEntity interest) {
        this.interests.remove(interest);
        interest.getUsers().remove(this);
    }
}
/*
package entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "createdAt", nullable = false, unique = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "isAdmin", nullable = false, unique = false, updatable = true)
    private boolean isAdmin;

    @Column(name = "isPublicProfile", nullable = false, unique = false, updatable = true)
    private boolean isPublicProfile;

    @Column(name = "isConfirmed", nullable = false, unique = false, updatable = true)
    private boolean isConfirmed;

    @Column(name = "isActive", nullable = false, unique = false, updatable = true)
    private boolean isActive = true;

    @Column(name = "emailToken", nullable = true, unique = false, updatable = true)
    private String emailToken;

    @Column(name = "emailTokenExpires", nullable = true, unique = false, updatable = true)
    private LocalDateTime emailTokenExpires;

    @ManyToOne
    @JoinColumn(name = "workplace_id", nullable = false)
    private WorkplaceEntity workplace;


    @ManyToMany(mappedBy = "Skill")
    private List<SkillEntity> skills = new ArrayList<>();



    // COMENTÁRIO
    //falta associar
    //interesses, skills,projetos, mensagens....




    public UserEntity() {
    createdAt = LocalDateTime.now();
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public List<SessionTokenEntity> getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(List<SessionTokenEntity> sessionToken) {
        this.sessionToken = sessionToken;
    }
}
*/
