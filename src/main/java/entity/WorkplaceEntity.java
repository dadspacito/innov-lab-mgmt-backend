package entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.LazyToOne;


import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name="Workplace")
@NamedQueries(
        {
                @NamedQuery(name = "Workplace.findWorkplaceByLocation", query = "SELECT w FROM WorkplaceEntity w WHERE w.location = :location"),
                @NamedQuery(name = "Workplace.findAllWorkplaces", query = "SELECT w FROM WorkplaceEntity w"),
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
    @OneToMany(mappedBy = "workplace")
    private List<UserEntity> users;
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

    public boolean isActive() {
        return active;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public Set<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectEntity> projects) {
        this.projects = projects;
    }

    /**
     * estas funções tem como objetivo adicionar ou remover projetos e workplaces da lista de projetos
     * @param p
     */
    //adicionar projetos a este workplace
    public void addProjectToWorkplace(ProjectEntity p){
        if (!this.projects.contains(p)) {
            this.projects.add(p);
            if (p.getProjectWorkplace() != this) {
                p.setProjectWorkplace(this);
            }
        }
    }
    //remover projetos deste workplace
    public void removeProjectFromWorkplace(ProjectEntity p){
        if (this.projects.contains(p)) {
            this.projects.remove(p);
            if (p.getProjectWorkplace() == this) {
                p.setProjectWorkplace(null);
            }
        }
    }

}

