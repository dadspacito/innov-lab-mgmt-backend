package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import entity.*;
import enums.ProjectState;
import org.hibernate.jdbc.Work;

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
    private ProjectManagerDto projectManager;
    //este recebe a projectMembersDto
    private Set<ProjectMemberDto> projectMembers;
    private Set<InterestDto> projectInterests;
    private Set<SkillDto> projectSkills;
    private Set<MaterialDto> projectMaterials;
    private Set<TaskDto> projectTasks;
    private WorkplaceDto projectWorkplace;

    public DetailedProjectDto(){}

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

    public ProjectState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public ProjectManagerDto getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManagerDto projectManager) {
        this.projectManager = projectManager;
    }

    public Set<ProjectMemberDto> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<ProjectMemberDto> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public Set<InterestDto> getProjectInterests() {
        return projectInterests;
    }

    public void setProjectInterests(Set<InterestDto> projectInterests) {
        this.projectInterests = projectInterests;
    }

    public Set<SkillDto> getProjectSkills() {
        return projectSkills;
    }

    public void setProjectSkills(Set<SkillDto> projectSkills) {
        this.projectSkills = projectSkills;
    }

    public Set<MaterialDto> getProjectMaterials() {
        return projectMaterials;
    }

    public void setProjectMaterials(Set<MaterialDto> projectMaterials) {
        this.projectMaterials = projectMaterials;
    }

   public Set<TaskDto> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(Set<TaskDto> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public WorkplaceDto getProjectWorkplace() {
        return projectWorkplace;
    }

    public void setProjectWorkplace(WorkplaceDto workplace) {
        this.projectWorkplace = workplace;
    }

}
