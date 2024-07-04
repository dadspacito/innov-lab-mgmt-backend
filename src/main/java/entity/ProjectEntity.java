package entity;

import enums.ProjectState;
import enums.SkillType;
import jakarta.persistence.*;
import jdk.jfr.Name;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="Project")
@NamedQueries({
        /**
         * que queries são necessárias para esta entity?
         * ir buscar projecto pelo ID
         * retornar as tasks de um projeto - isto é referente a entidade da task, que retorna as tasks desse projeto pelo id do projeto na task entity
         * retornar os users de um projeto
         * retornar os interesses de um projeto
         * retornar as skills de um projeto
         *
         *
         */
        @NamedQuery(name = "Project.getProjectByID", query = "select p from ProjectEntity p where p.id = :id" ),
        @NamedQuery(name = "Project.getMembers", query ="select distinct u from ProjectEntity p join p.projectMembers u where p.id = :id"),
        @NamedQuery(name = "Project.getInterests", query ="select distinct i from ProjectEntity p join p.interests i where p.id = :id"),
        @NamedQuery(name = "Project.getSkills", query = "select distinct s from ProjectEntity p join p.skills s where p.id = :id"),
        @NamedQuery(name = "Project.getMaterials", query="select m from MaterialEntity m where m.project.id = :id"),
        @NamedQuery(name = "Project.getTasks", query ="select t from TaskEntity t where t.project.id = :id"),
        //este get all retorna por ordem de data e por state
        @NamedQuery(name ="Project.getAll", query = "select p from ProjectEntity p order by p.startDate asc, p.projectState asc")
        //falta query de get projects by workplace





})
public class ProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name="name", nullable = false, unique = true, updatable = false)
    private String name;
    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;
    @Column(name="startDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;
    @Column(name="endDate", nullable = true, unique = false, updatable = true)
    private LocalDateTime endDate;
    @Enumerated
    @Column(name = "projectState", nullable = false, unique = false, updatable = true)
    private ProjectState projectState;


    /**
     * o que é mapped
     * users-many to many
     * interests- many to many
     * materials-one to many
     * skills- many to many
     * tasks- one to many
     * workplaces many to one
     */

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private UserEntity manager;

    // Many-to-Many relationship with UserEntity
    @ManyToMany
    @JoinTable(
            name = "project_members", // Join table name
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key in join table referencing ProjectEntity
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key in join table referencing UserEntity

    )
    private Set<UserEntity> projectMembers = new HashSet<>();

    // Many-to-Many relationship with InterestEntity
    @ManyToMany
    @JoinTable(
            name = "project_interests", // Join table name
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key in join table referencing ProjectEntity
            inverseJoinColumns = @JoinColumn(name = "interest_id") // Foreign key in join table referencing InterestEntity
    )
    private Set<InterestEntity> interests = new HashSet<>();

    // One-to-Many relationship with MaterialEntity
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MaterialEntity> materials = new HashSet<>();

    // Many-to-Many relationship with SkillEntity
    @ManyToMany
    @JoinTable(
            name = "project_skills", // Join table name
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key in join table referencing ProjectEntity
            inverseJoinColumns = @JoinColumn(name = "skill_id") // Foreign key in join table referencing SkillEntity
    )
    private Set<SkillEntity> skills = new HashSet<>();

    // One-to-Many relationship with TaskEntity
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskEntity> tasks = new HashSet<>();

    //one to many relationships with workplaces
    @ManyToOne
    @JoinColumn(name ="workplace_id", nullable = false)
    private WorkplaceEntity projectWorkplace;


    //contrutor vazio
    public ProjectEntity(){}
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

    public UserEntity getManager() {
        return manager;
    }

    public void setManager(UserEntity manager) {
        this.manager = manager;
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

    public ProjectState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public Set<UserEntity> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<UserEntity> users) {
        this.projectMembers = users;
    }

    public Set<InterestEntity> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestEntity> interests) {
        this.interests = interests;
    }

    public Set<MaterialEntity> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<MaterialEntity> materials) {
        this.materials = materials;
    }

    public Set<SkillEntity> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillEntity> skills) {
        this.skills = skills;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public WorkplaceEntity getProjectWorkplace() {
        return projectWorkplace;
    }

    public void setProjectWorkplace(WorkplaceEntity workplace) {
        if (this.projectWorkplace != null && this.projectWorkplace != workplace) {
            this.projectWorkplace.removeProjectFromWorkplace(this);
        }
        this.projectWorkplace = workplace;
        if (workplace != null && !workplace.getProjects().contains(this)) {
            workplace.getProjects().add(this);
        }
    }

    //faz se override ao equals e hashcode methods
    //este override o que faz é descriminar e dar consistencia aos equals que se utilizam para pesquisar, fazer merge
    //e outras funções que requerem comparação de elementos dentro da base de dados
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ProjectEntity)) return false;
        ProjectEntity that = (ProjectEntity) o;
        return id == that.id;
    }

    //este método define que a chave/codigo do hash é o ID o que melhora a performance de buscar de elementos
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
    //metodo para listas
    //metodos para adicionar interesses, skills, tasks, materiais e users ao projeto.

    /**
     * metodos set
     * @param member
     */
    public void addMember(UserEntity member){
        this.projectMembers.add(member);
        member.getProjects().add(this);
    }
    public void removeMember(UserEntity member){
        this.projectMembers.remove(member);
        member.getProjects().remove(this);//
    }
    //em materiais a construção é diferente porque a relação é one to many
    //neste cenario o que o set project está a apontar é a referencia aos materiais
    public void addMaterial(MaterialEntity material){
        this.materials.add(material);
        material.setProject(this);
    }
    public void removeMaterial(MaterialEntity material){
        if (this.materials.contains(material)){
            this.materials.remove(material);
            material.setProject(null);

        }    }
    public void addSkill(SkillEntity skill){
        this.skills.add(skill);
        skill.getProjects().add(this);
    }
    public void removeSkill(SkillEntity skill){
        this.skills.remove(skill);
        skill.getProjects().remove(this);
    }
    public void addInterest(InterestEntity interest){
        this.interests.add(interest);
        interest.getProjects().add(this);
    }
    public void removeInterest(InterestEntity interest){
        this.interests.remove(interest);
        interest.getProjects().remove(this);
    }
    public void addTask(TaskEntity task){
        this.tasks.add(task);
        task.setProject(this);
    }
    public void removeTask(TaskEntity task){
        this.tasks.remove(task);
        task.setProject(null);
    }

    //este método pode tendencialmente dar alguns problemas
    //faz sentido implementar aqui um remove projects?
    public void removeWorkplaceFromProject(WorkplaceEntity w){
            // Remove the project from the current workplace
            this.projectWorkplace.removeProjectFromWorkplace(this);
            // Set the workplace to null
            this.projectWorkplace = null;

    }
    public void addProjectManager(UserEntity manager){
        this.setManager(manager);
        manager.getManagedProjects().add(this);
    }


}
