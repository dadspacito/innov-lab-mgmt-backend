/*package entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name="Interest")
public class InterestEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="name", nullable = false, unique = true, updatable = true)
    private String name;



    public InterestEntity()
    {
    }

    public InterestEntity(long id, String name, String type)
    {
        this.id = id;
        this.name = name;

    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
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


}*/
