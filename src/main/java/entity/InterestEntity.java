package entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Interest")
@NamedQueries(
        {
                @NamedQuery(name = "Interest.findInterestByName", query = "SELECT i FROM InterestEntity i WHERE i.name = :name"),
                @NamedQuery(name = "Interest.findAllInterests", query = "SELECT i FROM InterestEntity i")
        }
)



// COMENTÁRIO NAMED QUERIES EM FALTA
// ASSOCIAR A PROJETOS, para criar namedqueries com projetos
// ASSOCIAR A USERS, (done)


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

    @ManyToMany(mappedBy = "interests")
    private Set<UserEntity> users = new HashSet<>();


    // código para map de projetos
//    @ManyToMany(mappedBy = "keywords")
//    private Set<ProjectEntity> projects = new HashSet<>();
//




    public InterestEntity()
    {
        this.createdAt = LocalDateTime.now();
    }

    public InterestEntity(String name)
    {
        this.name = name;
        this.createdAt = LocalDateTime.now();

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

    public Set<UserEntity> getUsers()
    {
        return users;
    }

    public void setUsers(Set<UserEntity> users)
    {
        this.users = users;
    }

    // MÉTODOS PARA AS LISTAS
    // método para adicionar um user ao interesse

    public void addUser(UserEntity user)
    {
        this.users.add(user);
        user.getInterests().add(this);
    }

    // método para remover um user do interesse

    public void removeUser(UserEntity user)
    {
        this.users.remove(user);
        user.getInterests().remove(this);
    }


}