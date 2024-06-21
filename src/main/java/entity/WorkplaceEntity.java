package entity;

import java.util.ArrayList;
import java.util.List;

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
                @NamedQuery(name = "Workplace.findAllWorkplaces", query = "SELECT w FROM WorkplaceEntity w")
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


    /**
     * aqui tem de map à tabela dos users.
     *
     * one to many users
     * faz sentido retornar uma lista ou simplesmente um user?
     * a query retorna sempe uma lista, depois no dao e no transactional é que filtramos desse array o valor que queremos
     */
    @OneToMany(mappedBy = "workplace")
    private List<UserEntity> users;



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



 /*   public List<ProjectEntity> getProjects() {
        return projects;
    }*/

   /* public void setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
    }*/
}

