package api;

import dto.DetailedProjectDto;
import dto.TaskDto;
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
        System.out.println(project);
        System.out.println(project.getProjectMembers());
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

    @Path("/{projectID}/task/{taskID}")
    @DELETE//apagar tarefas
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTaskFromProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID,@PathParam("taskID") int taskID){
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
    @Path("/{projectID}/task/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTaskToProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, TaskDto task){
        if (sessionService.isTokenValid(token)){
            try{
                projectService.addNewTaskProject(projectID, task);
                return Response.status(200).entity("Task was sucessfully added to project").build();
            }
            catch (Exception e){
                System.err.println("Project does not exist");
                return Response.status(401).entity("Project does not exist").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
    }
    //adicionar membros
    //teoricamente tem de ser convidar membros e não adicionar diretamente
    @Path("/{projectID}/members/{userID}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMemberToProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, @PathParam("userID") int userID){
        if (sessionService.isTokenValid(token)){
            if (!projectService.projectIsFull(projectID)) {
                projectService.addProjectMember(projectID, userID);
                return Response.status(200).entity("Member added to the project").build();
            }
            else return Response.status(400).entity("Project is already full").build();
        }
        else return Response.status(401).entity("Token is invalid").build();
    }

    //remover membro de projeto
    @Path("/{projectID}/members/{memberID}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeMemberFromProject(@HeaderParam("token") String token, @PathParam("projectID") int projectID, @PathParam("memberID") int memberID){
        if (sessionService.isTokenValid(token)){
            projectService.removeProjectMembers(projectID, memberID);
            return Response.status(200).entity("Member was removed from project").build();
        }
        else return Response.status(400).entity("User was not found").build();
    }

}
