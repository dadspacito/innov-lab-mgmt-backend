package api;

import dto.TaskDto;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.engine.jdbc.ReaderInputStream;
import service.SessionService;
import service.TaskService;
/**
 * TaskResource class provides RESTful endpoints for managing tasks.
 */
@Path("/tasks")
public class TaskResource {
    @EJB private SessionService sessionService;
    @EJB private TaskService taskService;
    /**
     * Retrieves a task by its ID.
     *
     * @param token  the session token
     * @param taskID the task ID
     * @return the response containing the task DTO
     */
    @Path("/{taskID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskByID(@HeaderParam("token") String token, @PathParam("taskID") int taskID){
        if (sessionService.isTokenValid(token)){
            return Response.status(200).entity(taskService.getTaskByID(taskID)).build();
        }
        return Response.status(400).entity("invalid token").build();
    }
    /**
     * Updates a task.
     *
     * @param token     the session token
     * @param projectID the project ID
     * @param taskID    the task ID
     * @param taskDto   the task DTO
     * @return the response indicating the result of the operation
     */
    @Path("{projectID}/update/{taskID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@HeaderParam("token") String token,@PathParam("projectID") int projectID, @PathParam("taskID") int taskID, TaskDto taskDto){
        if (sessionService.isTokenValid(token)){
            taskService.updateTask(projectID,taskID,taskDto);
            return Response.status(200).entity("task was successfully updated").build();
        }
        return Response.status(400).entity("invalid token").build();
    }
}
