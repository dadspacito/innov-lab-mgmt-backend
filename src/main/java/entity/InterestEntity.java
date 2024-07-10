package entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="Interest")
@NamedQueries(
        {
                @NamedQuery(name = "Interest.findInterestByName", query = "SELECT i FROM InterestEntity i WHERE i.name = :name"),
                @NamedQuery(name = "Interest.findAllInterests", query = "SELECT i FROM InterestEntity i"),
                @NamedQuery(name ="Interest.findInterestByID", query = "select i from InterestEntity i where i.id =:id"),
                @NamedQuery(name = "Interest.findAllActiveInterests", query = "SELECT i FROM InterestEntity i where i.isActive = true")

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

    //para users
    @ManyToMany(mappedBy = "interests")
    private Set<UserEntity> users = new HashSet<>();

    //para projetos
    @ManyToMany(mappedBy = "interests")
    private Set<ProjectEntity> projects = new HashSet<>();


//aqui será keywords ou simplesmente interesses?
// código para map de projetos? Porque não ser so interesses no projeto que pode devolver um array de interesses e skills?

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
        this.isActive =  true;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectEntity> projects) {
        this.projects = projects;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterestEntity that = (InterestEntity) o;
        return id == that.id; // Compare based on ID for persisted entities
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use ID for hash code
    }



}