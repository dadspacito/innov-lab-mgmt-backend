package api;

import dto.WorkplaceDto;
import jakarta.ejb.EJB;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.WorkplaceService;

import java.util.concurrent.locks.ReadWriteLock;
/**
 * WorkplaceResource class provides RESTful endpoints for managing workplaces.
 */
@Path("/workplace")
public class WorkplaceResource {
    @EJB
    private WorkplaceService workplaceService;
    /**
     * Retrieves all workplaces.
     *
     * @return the response containing the list of workplaces
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkplaces(){
        try{
            return Response.status(200).entity(workplaceService.returnWorkplaces()).build();
        } catch (NullPointerException e){
            return Response.status(400).entity("could not return tasks").build();
        }
    }
    /**
     * Retrieves a workplace by its ID.
     *
     * @param id the workplace ID
     * @return the response containing the workplace DTO or an error message
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkplaceByID(@PathParam("id") int id){
        if(workplaceService.getWorkplaceByID(id) != null){
            WorkplaceDto workplace = workplaceService.getWorkplaceDto(workplaceService.getWorkplaceByID(id));
            return Response.status(200).entity(workplace).build();
        }
        return Response.status(400).entity("workplace not found").build();
    }


}
