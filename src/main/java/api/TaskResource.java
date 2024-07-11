package api;

import dto.TaskDto;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.engine.jdbc.ReaderInputStream;
import service.SessionService;
import service.TaskService;

@Path("/tasks")
public class TaskResource {
    @EJB private SessionService sessionService;
    @EJB private TaskService taskService;
    /**
     * que paths é que esta resource precisa?
     * criar task- precisa de verificar se o user que a está a criar é um user válido na DB e válido dentro do projeto
     * apagar task- mesmo principio do acima
     * receber tasks:
     * -ver validade do user como entity na DB
     * -ver validade do user como pertencente ao projeto
     * -filtrar se calhar tasks que ja estao completas
     * -retornar tasks associados a certo user
     * -retornar tasks associados a projeto com o respetivo state
     * -retornar task por id
     * -retornar tasks associados a user por projeto
     * -retornar tasks associados a user no geral
     *
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
