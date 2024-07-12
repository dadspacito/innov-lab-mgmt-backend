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
import java.util.Set;
/**
 * SkillResource class provides RESTful endpoints for managing skills.
 */
@Path("/skills")
public class SkillResource {

    @EJB
    private SkillService skillService;

    @EJB
    private SessionService sessionService;

    private static final Logger LOGGER = LogManager.getLogger(HeaderResource.class);
    /**
     * Retrieves all skills.
     *
     * @param token the session token
     * @return the response containing the set of all skills
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkills(@HeaderParam("token") String token) {
            if (sessionService.isTokenValid(token)) {
                Set<SkillDto> skills = skillService.getAllSkills();
                return Response.status(Response.Status.OK).entity(skills).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid token").build();

            };
    /**
     * Creates a new skill.
     *
     * @param token the session token
     * @param skill the skill DTO
     * @return the response indicating the result of the operation
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSkill(@HeaderParam("token") String token, SkillDto skill){
        if (sessionService.isTokenValid(token)){
            SkillEntity skillEnt = skillService.mapSkillDtoToEntity(skill);
            if (skillService.checkSkillValidity(skillEnt)){
                skillService.createSkill(skill);
                return Response.status(201).entity("Skill was succesfully added to db").build();
            }
            return Response.status(400).entity("Skill already exists").build();
        }
        return Response.status(401).entity("Invalid token").build();
    }
    /**
     * Inactivates a skill.
     *
     * @param token the session token
     * @param id the skill ID
     * @return the response indicating the result of the operation
     */
    @Path("/inactivate/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inactivateSkill(@HeaderParam("token") String token, @PathParam("id") int id){
        if(sessionService.isTokenValid(token)){
            skillService.inactivateSkill(id);
            LOGGER.info("Skill with ID " + id + " was successfully inactivated");
            return Response.status(200).entity("Skill was sucessfully inactivated").build();
        }
        return Response.status(400).entity("that user is not valid" +  id).build();
    }
    /**
     * Activates a skill.
     *
     * @param token the session token
     * @param id the skill ID
     * @return the response indicating the result of the operation
     */
    @Path("/activate/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response activateSkill(@HeaderParam("token") String token, @PathParam("id") int id){
        if (sessionService.isTokenValid(token)){
            skillService.activateSkill(id);
            LOGGER.info("Skill with ID " + id + " was successfully activated");
            return Response.status(200).entity("Skill was sucessfully activated").build();
        }
        return Response.status(400).entity("that user is not valid" +  id).build();
    }

}

