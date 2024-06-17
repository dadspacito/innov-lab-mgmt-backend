package entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name="Interest")

// COMENTÀRIO NAMED QUERIES EM FALTA
// ASSOCIAR A PROJETOS, para criar namedqueries com projetos
// ASSOCIAR A USERS, para criar namedqueries com users para adicionar users com interesses? tipo users sugeridos?

public class InterestEntity implements Serializable

{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="name", nullable = false, unique = true, updatable = true)
    private String name;

    @Column(name="isActive", nullable = false, unique = false, updatable = true)
    private boolean isActive = true;

    @Column (name = "createdAt", nullable = false, unique = false, updatable = false)
    private LocalDateTime createdAt;




    public InterestEntity()
    {
    }

    public InterestEntity(String name)
    {
        this.name = name;


    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(boolean active)
    {
        this.isActive = active;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

}