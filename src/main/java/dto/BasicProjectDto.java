package dto;

import entity.InterestEntity;
import entity.SkillEntity;
import enums.ProjectState;

import java.util.List;

public class BasicProjectDto {
    /**
     * este DTO retorna os projetos basicos para nao-users verem
     * nome,
     * detalhes
     *interesses e skills em forma de keywords? ou poe se so interesses e skills
     */

    private String name;
    private String description;
    private List<InterestDto> projectInterests;
    private List<SkillDto> projectSkills;
    private ProjectState projectState;


    public BasicProjectDto(){}
    public BasicProjectDto(String name, String description, List<InterestDto> projectInterests, List<SkillDto> projectSkills,
                           ProjectState projectState){
        this.name = name;
        this.description = description;
        this.projectInterests = projectInterests;
        this.projectSkills = projectSkills;
        this.projectState = projectState;
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

    public List<InterestDto> getProjectInterests() {
        return projectInterests;
    }

    public void setProjectInterests(List<InterestDto> interests) {
        this.projectInterests = interests;
    }

    public List<SkillDto> getProjectSkills() {
        return projectSkills;
    }

    public void setProjectSkills(List<SkillDto> skills) {
        this.projectSkills = skills;
    }

    public ProjectState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectState projectState) {
        this.projectState = projectState;
    }
}
