package entity;

import jakarta.persistence.*;
import net.bytebuddy.asm.Advice;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @Column(name="description", nullable = false, unique = true, updatable = false)
    private String description;
    @Column(name="startDate", nullable = false, unique = true, updatable = false)
    private LocalDateTime startDate;
    @Column(name="endDate", nullable = false, unique = true, updatable = false)
    private LocalDateTime endDate;

    /**
     * o que é mapped
     * users
     * interests
     * materials
     * skills
     */
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