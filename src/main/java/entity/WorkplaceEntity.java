package entity;

import java.util.*;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.LazyToOne;


import java.io.Serializable;


@Entity
@Table(name="Workplace")
@NamedQueries(
        {
                @NamedQuery(name = "Workplace.findWorkplaceByLocation", query = "SELECT w FROM WorkplaceEntity w WHERE w.location = :location"),
                @NamedQuery(name = "Workplace.findAllWorkplaces", query = "SELECT w FROM WorkplaceEntity w order by w.id asc"),
                @NamedQuery(name = "Workplace.findWorkplaceByID", query = "select w from WorkplaceEntity w where w.id = :id")
        }
)

public class WorkplaceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="location", nullable = false, unique = true, updatable = true)
    private String location;

    @Column(name="active", nullable = false, unique = false, updatable = true)
    private boolean active = true;

    //private List<ProjectEntity> projects = new ArrayList<>();


    /**
     * aqui tem de map à tabela dos users.
     *
     * one to many users
     * faz sentido retornar uma lista ou simplesmente um user?
     * a query retorna sempe uma lista, depois no dao e no transactional é que filtramos desse array o valor que queremos
     */
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.ALL)
    private Set<UserEntity> users;
    // One-to-Many relationship to Project
    @OneToMany(mappedBy = "projectWorkplace", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectEntity> projects = new HashSet<>();



// outra one to many. lista projetos por workplace




    public WorkplaceEntity() {
    }


    public WorkplaceEntity (String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;

    }

    public Set<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectEntity> projects) {
        this.projects = projects;
    }

    public boolean isActive() {
        return active;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }


    /**
     * estas funções tem como objetivo adicionar ou remover projetos e workplaces da lista de projetos
     * @param
     */
    //adicionar projetos a este workplace
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkplaceEntity that = (WorkplaceEntity) o;
        return id == that.id; // Compare based on ID for persisted entities
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use ID for hash code
    }

}

