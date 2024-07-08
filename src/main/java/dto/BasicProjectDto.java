package dto;

import entity.InterestEntity;
import entity.SkillEntity;
import enums.ProjectState;

import java.util.List;
import java.util.Set;

public class BasicProjectDto {
    /**
     * este DTO retorna os projetos basicos para nao-users verem
     * nome,
     * detalhes
     *interesses e skills em forma de keywords? ou poe se so interesses e skills
     */
    private int id;

    private String name;
    private String description;
    private Set<InterestDto> projectInterests;
    private Set<SkillDto> projectSkills;
    private ProjectState projectState;


    public BasicProjectDto(){}

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

    public Set<InterestDto> getProjectInterests() {
        return projectInterests;
    }

    public void setProjectInterests(Set<InterestDto> interests) {
        this.projectInterests = interests;
    }

    public Set<SkillDto> getProjectSkills() {
        return projectSkills;
    }

    public void setProjectSkills(Set<SkillDto> skills) {
        this.projectSkills = skills;
    }

    public ProjectState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
