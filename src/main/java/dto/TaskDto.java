package dto;
//o que é que ele vai retornar para ser visualizado?

import entity.ProjectEntity;
import entity.TaskEntity;
import entity.UserEntity;
import enums.TaskState;

import java.time.LocalDateTime;

public class TaskDto {
    private int id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TaskState state;
    //tem de retornar um project member Dto
    private int projectID;
    private ProjectMemberDto ownerID;

    //para construir a task inicial

    public TaskDto() {
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

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public ProjectMemberDto getOwner() {
        return ownerID;
    }

    public void setOwner(ProjectMemberDto ownerID) {
        this.ownerID = ownerID;
    }
}
