package entity;

import enums.TaskState;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jdk.jfr.Name;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@NamedQueries({
        /**
         * retornar todas as tasks de um projecto
         * retornar a task pelo seu id
         * retornar a task pelo seu owner_id
         */
        @NamedQuery(name="task.findByID", query = "select t from TaskEntity t where t.id = :id"),
        @NamedQuery(name = "task.findByOwnerID", query ="SELECT t from TaskEntity t where t.owner = :owner order by t.startDate asc, t.state asc"),
        @NamedQuery(name = "task.findByProjectID", query = "select t from TaskEntity t where t.project = :project order by t.startDate asc , t.state asc, t.id asc"),
        //aqui tem de haver queries que se insiram dentro do projeto?
        @NamedQuery(name = "task.findByProjectAndOwner", query = "select t from TaskEntity t where t.project = :project and t.owner = :owner order by t.startDate asc, t.state asc"),
        @NamedQuery(name = "task.findByProjectAndState", query = "select t from TaskEntity t where t.project = :project and t.state = :state order by t.startDate asc")


})
public class TaskEntity implements Serializable {
    /**
     * one to many com projects
     * nome, responsável, inicio, fim, prioridade
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "startDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;
    @Column(name = "endDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, unique = false, updatable = true)
    private TaskState state;

    /**
     * owner da task many to one- a user can have many tasks, but a task can only have one owner
     */
    //esta função tem de ser updatable se se quiser alterar dono da tarefa
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity owner;
    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    public TaskEntity(){};
    public TaskEntity(int id, String name, String description, LocalDateTime startDate, LocalDateTime endDate, UserEntity owner){
        this.name =  name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;

    }

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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
