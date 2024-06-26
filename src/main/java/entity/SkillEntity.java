package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import enums.SkillType;

@Entity
@Table(name = "Skill")
@NamedQueries({
        @NamedQuery(name = "Skill.findSkillByName", query = "SELECT s FROM SkillEntity s WHERE s.name = :name"),
        @NamedQuery(name = "Skill.findAllSkills", query = "SELECT s FROM SkillEntity s")
})
public class SkillEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = true)
    private SkillType type;

    @Column(name = "isActive", nullable = false, updatable = true)
    private boolean isActive = true;

    @Column (name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;



    @ManyToMany(mappedBy = "skills")
    private Set<UserEntity> users = new HashSet<>();


    public SkillEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public SkillEntity(String name, SkillType type) {
        this.name = name;
        this.type = type;
        this.createdAt = LocalDateTime.now();

    }

    // Getters and setters

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

    public SkillType getType() {
        return type;
    }

    public void setType(SkillType type) {
        this.type = type;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public Set<UserEntity> getUsers() {

        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;

    }

    // Método para adicionar um user à skill
    public void addUser(UserEntity user) {

        this.users.add(user);
        user.getSkills().add(this);
    }

    // Método para remover um user da skill
    public void removeUser(UserEntity user) {
        this.users.remove(user);
        user.getSkills().remove(this);

    }
}

