package api;


import dto.SkillDto;
import entity.SkillEntity;
import enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.SessionService;
import service.SkillService;

import java.util.List;

@Path("/skills")
public class SkillResource {

    /**
     * que endpoints faltam aqui?
     * retornar users por skill selecionada
     * adicionar skill
     * remover skill
     *
     */

    @EJB
    private SkillService skillService;

    @EJB
    private SessionService sessionService;

    private static final Logger LOGGER = LogManager.getLogger(HeaderResource.class);


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkills(@HeaderParam("token") String token) {

            if (sessionService.isTokenValid(token)) {
                List<SkillDto> skills = skillService.getAllSkills();
                return Response.status(Response.Status.OK).entity(skills).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid token").build();

            };
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, SkillDto skill){
        if (sessionService.isTokenValid(token)){
            System.out.println(skill);
            skillService.createSkill(skill);
            return Response.status(201).entity("Skill was succesfully added to db").build();
        }
        return Response.status(400).entity("Skill was not added DB").build();
    }

}

/*

public class SkillResource {

    private final SkillService skillService;

    @Inject
    public SkillResource(SkillService skillService) {
        this.skillService = skillService;
    }

    @GET
    @Path("/skills")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkills() {
        List<Skill> skills = skillService.getSkills();
        return Response.ok(skills).build();
    }

    @POST
    @Path("/skills")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkill(Skill skill) {
        Skill createdSkill = skillService.createSkill(skill);
        return Response.ok(createdSkill).build();
    }

    @PUT
    @Path("/skills/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkill(@PathParam("id") Long id, Skill skill) {
        Skill updatedSkill = skillService.updateSkill(id, skill);
        return Response.ok(updatedSkill).build();
    }

    @DELETE
    @Path("/skills/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSkill(@PathParam("id") Long id) {
        skillService.deleteSkill(id);
        return Response.ok().build();
    }

    @GET
    @Path("/skills/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkill(@PathParam("id") Long id) {
        Skill skill = skillService.getSkill(id);
        return Response.ok(skill).build();
    }

    @GET
    @Path("/skills/{id}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersWithSkill(@PathParam("id") Long id) {
        List<User> users = skillService.getUsersWithSkill(id);
        return Response.ok(users).build();
    }

    @POST
    @Path("/skills/{id}/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserSkill(@PathParam("id") Long id, @PathParam("userId") Long userId) {
        skillService.addUserSkill(id, userId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/skills/{id}/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserSkill(@PathParam("id") Long id, @PathParam("userId") Long userId) {
        skillService.removeUserSkill(id, userId);
        return Response
}
*/
