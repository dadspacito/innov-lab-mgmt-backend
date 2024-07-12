package api;

import dto.DetailedProjectDto;
import dto.TaskDto;
import jakarta.ejb.EJB;
import jakarta.ejb.NoSuchEntityException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.*;

/**
 * ProjectResource class provides RESTful endpoints for managing projects.
 */
@Path("/projects")
public class ProjectResource {
    @EJB
    private MaterialService materialService;
    @EJB private UserService userService;
    @EJB private SessionService sessionService;
    @EJB private TaskService taskService;
    @EJB private SkillService skillService;
    @EJB private ProjectService projectService;
    private static final Logger LOGGER = LogManager.getLogger(ProjectResource.class);

    /**
     * Creates a new project.
     *
     * @param token the session token
     * @param project the detailed project DTO
     * @return the response indicating the result of the operation
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewProject(@HeaderParam("token") String token, DetailedProjectDto project){
        if (sessionService.isTokenValid(token)) {
            try {
                projectService.createNewProject(project);
                LOGGER.info("New project was created for user" + project.getProjectManager().getName());
                return Response.status(Response.Status.OK).entity("Project was created").build();
            }
            catch (IllegalArgumentException e){
                LOGGER.error("Project was not created" + project.getProjectManager().getName());
                return Response.status(Response.Status.BAD_REQUEST).entity("project was not created, bad request").build();
            }
        }
        LOGGER.error("Unauthorized user trying to create token " + userService.getUserByToken(token).getEmail());
        return Response.status(Response.Status.UNAUTHORIZED).entity("invalid token").build();
    }
    /**
     * Removes a task from a project.
     *
     * @param token the session token
     * @param projectID the project ID
     * @param taskID the task ID
     * @return the response indicating the result of the operation
     */
    @Path("/{projectID}/task/{taskID}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTaskFromProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID,@PathParam("taskID") int taskID){
        if (sessionService.isTokenValid(token)){
            try{
                projectService.removeTaskFromProject(taskID, projectID);
                LOGGER.info("task was removed to project " + projectService.getDetailedProject(projectID).getName() + "by " + userService.getUserByToken(token).getEmail());
                return Response.status(200).entity("rask removed").build();
            }
            catch (NoSuchEntityException e){
                LOGGER.error("Task was not removed to project " + projectService.getDetailedProject(projectID).getName());
                return Response.status(400).entity("problem").build();
            }
        }
        LOGGER.error("Invalid token access " + userService.getUserByToken(token).getEmail());
        return Response.status(401).entity("invalid token").build();
    }
    /**
     * Adds a task to a project.
     *
     * @param token the session token
     * @param projectID the project ID
     * @param task the task DTO
     * @return the response indicating the result of the operation
     */
    @Path("/{projectID}/task/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTaskToProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, TaskDto task){
        if (sessionService.isTokenValid(token)){
            try{
                projectService.addNewTaskProject(projectID, task);
                LOGGER.info("task was added to project " + projectService.getDetailedProject(projectID).getName() + "by " + userService.getUserByToken(token).getEmail());
                return Response.status(200).entity("Task was sucessfully added to project").build();
            }
            catch (Exception e){
                LOGGER.error("Task was not added to project " + projectService.getDetailedProject(projectID).getName());
                return Response.status(401).entity("Project does not exist").build();
            }
        }
        LOGGER.error("Invalid token access " + userService.getUserByToken(token).getEmail());
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
    }
    /**
     * Adds a member to a project.
     *
     * @param token the session token
     * @param projectID the project ID
     * @param userID the user ID
     * @return the response indicating the result of the operation
     */
    @Path("/{projectID}/members/{userID}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMemberToProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, @PathParam("userID") int userID){
        if (sessionService.isTokenValid(token)){
            if (!projectService.projectIsFull(projectID)) {
                projectService.addProjectMember(projectID, userID);
                LOGGER.info("Member" + userService.getUserByID(userID).getEmail() + " was added to project " + projectService.getDetailedProject(projectID).getName() + "by " + userService.getUserByToken(token).getEmail());
                return Response.status(200).entity("Member added to the project").build();
            }

            else{
                LOGGER.info("Member" + userService.getUserByID(userID).getEmail() + " was not added to project " + projectService.getDetailedProject(projectID).getName() + "by " + userService.getUserByToken(token).getEmail());
                return Response.status(400).entity("Project is already full").build();
            }
        }
        else{
            LOGGER.error("Invalid token access " + userService.getUserByToken(token).getEmail());
            return Response.status(401).entity("Token is invalid").build();
        }
    }
    /**
     * Removes a member from a project.
     *
     * @param token the session token
     * @param projectID the project ID
     * @param memberID the member ID
     * @return the response indicating the result of the operation
     */
    @Path("/{projectID}/members/{memberID}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeMemberFromProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, @PathParam("memberID") int memberID){
        if (sessionService.isTokenValid(token)){
            projectService.removeProjectMembers(projectID, memberID);
            LOGGER.info("Member" + userService.getUserByID(memberID).getEmail() + " was removed to project " + projectService.getDetailedProject(projectID).getName() + "by " + userService.getUserByToken(token).getEmail());
            return Response.status(200).entity("Member was removed from project").build();
        }
        else{
            LOGGER.info("Member" + userService.getUserByID(memberID).getEmail() + " was not removed to project " + projectService.getDetailedProject(projectID).getName() + "by " + userService.getUserByToken(token).getEmail());
            return Response.status(400).entity("User was not found").build();
        }
    }
    /**
     * Retrieves all basic projects.
     *
     * @return the response containing the list of basic projects
     */
    @Path("/basicProject")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBasicProjects(){
        return Response.status(200).entity(projectService.getBasicProjects()).build();
    }
    /**
     * Retrieves the detailed project information.
     *
     * @param token the session token
     * @param projectID the project ID
     * @return the response containing the detailed project information
     */
    @Path("/detailedProject/{projectID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetailedProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID){
        if (sessionService.isTokenValid(token)){
            return Response.status(200).entity(projectService.getDetailedProject(projectID)).build();
        }
        return Response.status(400).entity("invalid token").build();
    }
    /**
     * Updates a project.
     *
     * @param token the session token
     * @param projectID the project ID
     * @param project the detailed project DTO
     * @return the response indicating the result of the operation
     */
    @Path("/update/{projectID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, DetailedProjectDto project){
        if (sessionService.isTokenValid(token)){
            projectService.updateProject(projectID, project);
            return Response.status(200).entity("Project was successfully updated").build();
        }
        return Response.status(400).entity("Invalid token").build();
    }
}
