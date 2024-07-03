package dto;

import entity.*;
import enums.ProjectState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DetailedProjectDto {
    /**
     * este DTO contem toda a informação sobre projectos para users que estejam logged in
     *id, name, description, Start date, end date, project state, manager,
     * members [], interests[], skills[], tasks[], materiais[]
     * cada vez que houver uma alteração em qualquer projeto, tem de haver uma notificação que alerta para a
     * mudança e o user que mudou;
     */
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectState projectState;
    private ProjectMemberDto projectManager;
    //este recebe a projectMembersDto
    private List<ProjectMemberDto> projectMembers;
    private List<InterestDto> projectInterests;
    private List<SkillDto> projectSkills;
    private List<MaterialDto> projectMaterials;
    private List<TaskDto> projectTasks;
    private WorkplaceEntity workplace;

    public DetailedProjectDto(){}
    public DetailedProjectDto(String name,String description, LocalDateTime startDate, LocalDateTime endDate,
     ProjectState projectState, ProjectMemberDto projectManager, List<ProjectMemberDto> projectMembers, List<InterestDto> projectInterests,
                              List<SkillDto> projectSkills, List<MaterialDto> projectMaterials, List<TaskDto> projectTasks){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectState = projectState;
        this.projectManager = projectManager;


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

    public ProjectMemberDto getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectMemberDto projectManager) {
        this.projectManager = projectManager;
    }

    public List<ProjectMemberDto> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<ProjectMemberDto> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public List<InterestDto> getProjectInterests() {
        return projectInterests;
    }

    public void setProjectInterests(List<InterestDto> projectInterests) {
        this.projectInterests = projectInterests;
    }

    public List<SkillDto> getProjectSkills() {
        return projectSkills;
    }

    public void setProjectSkills(List<SkillDto> projectSkills) {
        this.projectSkills = projectSkills;
    }

    public List<MaterialDto> getProjectMaterials() {
        return projectMaterials;
    }

    public void setProjectMaterials(List<MaterialDto> projectMaterials) {
        this.projectMaterials = projectMaterials;
    }

    public List<TaskDto> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(List<TaskDto> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public WorkplaceEntity getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceEntity workplace) {
        this.workplace = workplace;
    }

}
