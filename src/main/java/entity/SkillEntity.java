package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
import enums.SkillType;



@Entity
@Table(name="Skill")
@NamedQueries(
        {
                @NamedQuery(name = "Skill.findSkillByName", query = "SELECT s FROM SkillEntity s WHERE s.name = :name"),
                @NamedQuery(name = "Skill.findAllSkills", query = "SELECT s FROM SkillEntity s")
        }

)


public class SkillEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="name", nullable = false, unique = true, updatable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false, unique = false, updatable = true)
    private SkillType type;


    @Column(name="isActive", nullable = false, unique = false, updatable = true)
    private boolean isActive = true;

    public SkillEntity()
    {
    }

    public SkillEntity(String name, SkillType type)
    {
        this.name = name;
        this.type = type;
    }

    public long getId()
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

    public SkillType getType()
    {
        return type;
    }

    public void setType(SkillType type)
    {
        this.type = type;
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }
    
}
