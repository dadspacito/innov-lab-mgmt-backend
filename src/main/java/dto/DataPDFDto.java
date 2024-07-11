package dto;

import java.util.Map;
import java.util.Set;

public class DataPDFDto {
    /**
     * Set of project names
     */
    private Set<String> projectNames;

    /**
     * Map of workplace names to the number of submitted projects
     */
    private Map<String, Integer> submittedProjectsByWorkplace;

    /**
     * Map of workplace names to the percentage of submitted projects
     */
    private Map<String, Double> submittedProjectsPercentageByWorkplace;

    /**
     * Mean number of project members per project
     */
    private Double meanNumberOfProjectMembersPerProject;

    /**
     * Map of workplace names to the number of approved projects
     */
    private Map<String, Integer> approvedProjectsByWorkplace;

    /**
     * Map of workplace names to the percentage of approved projects
     */
    private Map<String, Double> approvedProjectsPercentageByWorkplace;

    /**
     * Map of workplace names to the number of terminated projects
     */
    private Map<String, Integer> terminatedProjectsByWorkplace;

    /**
     * Map of workplace names to the percentage of terminated projects
     */
    private Map<String, Double> terminatedProjectsPercentageByWorkplace;

    /**
     * Map of workplace names to the number of canceled projects
     */
    private Map<String, Integer> canceledProjectsByWorkplace;

    /**
     * Map of workplace names to the percentage of canceled projects
     */
    private Map<String, Double> canceledProjectsPercentageByWorkplace;

    /**
     * Mean project execution time in days
     */
    private Double meanProjectExecutionTime;

    // Getters and Setters

    public Set<String> getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(Set<String> projectNames) {
        this.projectNames = projectNames;
    }

    public Map<String, Integer> getSubmittedProjectsByWorkplace() {
        return submittedProjectsByWorkplace;
    }

    public void setSubmittedProjectsByWorkplace(Map<String, Integer> submittedProjectsByWorkplace) {
        this.submittedProjectsByWorkplace = submittedProjectsByWorkplace;
    }

    public Map<String, Double> getSubmittedProjectsPercentageByWorkplace() {
        return submittedProjectsPercentageByWorkplace;
    }

    public void setSubmittedProjectsPercentageByWorkplace(Map<String, Double> submittedProjectsPercentageByWorkplace) {
        this.submittedProjectsPercentageByWorkplace = submittedProjectsPercentageByWorkplace;
    }

    public Double getMeanNumberOfProjectMembersPerProject() {
        return meanNumberOfProjectMembersPerProject;
    }

    public void setMeanNumberOfProjectMembersPerProject(Double meanNumberOfProjectMembersPerProject) {
        this.meanNumberOfProjectMembersPerProject = meanNumberOfProjectMembersPerProject;
    }

    public Map<String, Integer> getApprovedProjectsByWorkplace() {
        return approvedProjectsByWorkplace;
    }

    public void setApprovedProjectsByWorkplace(Map<String, Integer> approvedProjectsByWorkplace) {
        this.approvedProjectsByWorkplace = approvedProjectsByWorkplace;
    }

    public Map<String, Double> getApprovedProjectsPercentageByWorkplace() {
        return approvedProjectsPercentageByWorkplace;
    }

    public void setApprovedProjectsPercentageByWorkplace(Map<String, Double> approvedProjectsPercentageByWorkplace) {
        this.approvedProjectsPercentageByWorkplace = approvedProjectsPercentageByWorkplace;
    }

    public Map<String, Integer> getTerminatedProjectsByWorkplace() {
        return terminatedProjectsByWorkplace;
    }

    public void setTerminatedProjectsByWorkplace(Map<String, Integer> terminatedProjectsByWorkplace) {
        this.terminatedProjectsByWorkplace = terminatedProjectsByWorkplace;
    }

    public Map<String, Double> getTerminatedProjectsPercentageByWorkplace() {
        return terminatedProjectsPercentageByWorkplace;
    }

    public void setTerminatedProjectsPercentageByWorkplace(Map<String, Double> terminatedProjectsPercentageByWorkplace) {
        this.terminatedProjectsPercentageByWorkplace = terminatedProjectsPercentageByWorkplace;
    }

    public Map<String, Integer> getCanceledProjectsByWorkplace() {
        return canceledProjectsByWorkplace;
    }

    public void setCanceledProjectsByWorkplace(Map<String, Integer> canceledProjectsByWorkplace) {
        this.canceledProjectsByWorkplace = canceledProjectsByWorkplace;
    }

    public Map<String, Double> getCanceledProjectsPercentageByWorkplace() {
        return canceledProjectsPercentageByWorkplace;
    }

    public void setCanceledProjectsPercentageByWorkplace(Map<String, Double> canceledProjectsPercentageByWorkplace) {
        this.canceledProjectsPercentageByWorkplace = canceledProjectsPercentageByWorkplace;
    }

    public Double getMeanProjectExecutionTime() {
        return meanProjectExecutionTime;
    }

    public void setMeanProjectExecutionTime(Double meanProjectExecutionTime) {
        this.meanProjectExecutionTime = meanProjectExecutionTime;
    }
}
