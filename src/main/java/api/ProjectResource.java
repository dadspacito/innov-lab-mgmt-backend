package api;

import dto.DetailedProjectDto;
import jakarta.ejb.EJB;
import jakarta.ejb.NoSuchEntityException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.*;

@Path("/projects")
public class ProjectResource {
    @EJB
    private MaterialService materialService; //verificar se materiais existem
    @EJB private UserService userService; //verificar se users existem para os membros
    @EJB private SessionService sessionService; //verificar token de criação
    @EJB private TaskService taskService;//para adicionar tasks ao projeto
    @EJB private SkillService skillService;//verificar skills na criação
    @EJB private ProjectService projectService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewProject(@HeaderParam("token") String token, DetailedProjectDto project){
        if (sessionService.isTokenValid(token)) {
            try {
                projectService.createNewProject(project);
                return Response.status(Response.Status.OK).entity("Project was created").build();
            }
            catch (IllegalArgumentException e){
                System.err.println("project was not created" + e);
                return Response.status(Response.Status.BAD_REQUEST).entity("project was not created, bad request").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("invalid token").build();
    }
    @GET//retornar todos os projetos
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@HeaderParam("token") String token){
        if (sessionService.isTokenValid(token)){

            return Response.status(Response.Status.OK).entity(projectService.getProjects()).build();
        }
        else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("/{id}")
    @DELETE//apagar tarefas
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTaskFromProject(@HeaderParam("token") String token, @PathParam("id") int projectID, int taskID){
        if (sessionService.isTokenValid(token)){
            try{
                projectService.removeTaskFromProject(taskID, projectID);
                return Response.status(200).entity("rask removed").build();
            }
            catch (NoSuchEntityException e){
                return Response.status(400).entity("problem").build();
            }
        }
        return Response.status(401).entity("invalid token").build();
    }

}
