package entity;

import enums.ProjectState;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Project")
public class ProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name="name", nullable = false, unique = true, updatable = false)
    private String name;
    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;
    @Column(name="startDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;
    @Column(name="endDate", nullable = true, unique = false, updatable = true)
    private LocalDateTime endDate;
    @Enumerated
    @Column(name = "projectState", nullable = false, unique = false, updatable = true)
    private ProjectState projectState;


    /**
     * o que é mapped
     * users-many to many
     * interests- many to many
     * materials-one to many
     * skills- many to many
     * tasks- one to many
     */

    // Many-to-Many relationship with UserEntity
    @ManyToMany
    @JoinTable(
            name = "project_users", // Join table name
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key in join table referencing ProjectEntity
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key in join table referencing UserEntity
    )
    private Set<UserEntity> users = new HashSet<>();

    // Many-to-Many relationship with InterestEntity
    @ManyToMany
    @JoinTable(
            name = "project_interests", // Join table name
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key in join table referencing ProjectEntity
            inverseJoinColumns = @JoinColumn(name = "interest_id") // Foreign key in join table referencing InterestEntity
    )
    private Set<InterestEntity> interests = new HashSet<>();

    // One-to-Many relationship with MaterialEntity
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MaterialEntity> materials = new HashSet<>();

    // Many-to-Many relationship with SkillEntity
    @ManyToMany
    @JoinTable(
            name = "project_skills", // Join table name
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key in join table referencing ProjectEntity
            inverseJoinColumns = @JoinColumn(name = "skill_id") // Foreign key in join table referencing SkillEntity
    )
    private Set<SkillEntity> skills = new HashSet<>();

    // One-to-Many relationship with TaskEntity
    /*@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskEntity> tasks = new HashSet<>();*/



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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
