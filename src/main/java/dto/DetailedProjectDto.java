package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.*;
import enums.ProjectState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class DetailedProjectDto {
    /**
     * este DTO contem toda a informação sobre projectos para users que estejam logged in
     *id, name, description, Start date, end date, project state, manager,
     * members [], interests[], skills[], tasks[], materiais[]
     * cada vez que houver uma alteração em qualquer projeto, tem de haver uma notificação que alerta para a
     * mudança e o user que mudou;
     */
    private int id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectState projectState;
    private int projectManager;
    //este recebe a projectMembersDto
    private Set<Integer> projectMembers;
    private Set<Integer> projectInterests;
    private Set<Integer> projectSkills;
    private Set<Integer> projectMaterials;
    private Set<Integer> projectTasks;
    @JsonProperty("workplaceID")
    private int workplaceID;

    public DetailedProjectDto(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkplaceID() {
        return workplaceID;
    }

    public void setWorkplaceID(int workplaceID) {
        this.workplaceID = workplaceID;
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

    public ProjectState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public int getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(int projectManager) {
        this.projectManager = projectManager;
    }

    public Set<Integer> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<Integer> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public Set<Integer> getProjectInterests() {
        return projectInterests;
    }

    public void setProjectInterests(Set<Integer> projectInterests) {
        this.projectInterests = projectInterests;
    }

    public Set<Integer> getProjectSkills() {
        return projectSkills;
    }

    public void setProjectSkills(Set<Integer> projectSkills) {
        this.projectSkills = projectSkills;
    }

    public Set<Integer> getProjectMaterials() {
        return projectMaterials;
    }

    public void setProjectMaterials(Set<Integer> projectMaterials) {
        this.projectMaterials = projectMaterials;
    }

   public Set<Integer> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(Set<Integer> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public int getWorkplace() {
        return workplaceID;
    }

    public void setWorkplace(int workplace) {
        this.workplaceID = workplace;
    }

}
