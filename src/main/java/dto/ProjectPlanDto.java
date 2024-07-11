package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

public class ProjectPlanDto {

    private Set<TaskDto> taskList;
    private String projectDuration;
    private String additionalExecutors;

    public ProjectPlanDto() {
    }

    public Set<TaskDto> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<TaskDto> taskList) {
        this.taskList = taskList;
        setProjectDuration(taskList);
    }

    public String getProjectDuration() {
        return projectDuration;
    }
    private void setProjectDuration(Set<TaskDto> tasks) {
        Period totalDuration = Period.ZERO; // Initialize total duration to zero
        for (TaskDto t : tasks) {
            LocalDate startDate = t.getStartDate().toLocalDate();
            LocalDate endDate = t.getEndDate().toLocalDate();
            // Calculate the period between start and end dates
            Period taskPeriod = Period.between(startDate, endDate);
            // Add the task period to the total duration
            totalDuration = totalDuration.plus(taskPeriod);
        }
        // Convert the total duration to a readable format (e.g., "X months Y days")
        this.projectDuration = totalDuration.getMonths() + " months " + totalDuration.getDays() + " days";
    }



    public String getAditionalExecutors() {
        return additionalExecutors;
    }

    public void setAditionalExecutors(String aditionalExecutors) {
        this.additionalExecutors = aditionalExecutors;
    }

}
