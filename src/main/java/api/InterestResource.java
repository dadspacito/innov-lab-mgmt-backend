
package api;

import dto.InterestDto;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.SessionService;
import service.InterestService;

import java.util.List;

@Path("/interests")
public class InterestResource {

    @EJB
    private InterestService interestService;

    @EJB
    private SessionService sessionService;

    private static final Logger LOGGER = LogManager.getLogger(HeaderResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInterests(@HeaderParam("token") String token) {
        if (sessionService.isTokenValid(token)) {
            List<InterestDto> interests = interestService.getAllInterests();
            return Response.status(Response.Status.OK).entity(interests).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid token").build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewInterest(@HeaderParam("token") String token, InterestDto interestDto){
        if(sessionService.isTokenValid(token)){
            interestService.createNewInterest(interestDto);
            return Response.status(Response.Status.CREATED).entity("new interest added").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("invalid Token").build();
    }
    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeInterest(@HeaderParam("token") String token, @PathParam("id") int id){
        if (sessionService.isTokenValid(token)){
            interestService.removeInterest(id);
            return Response.status(Response.Status.OK).entity("interest removed from BD").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
    }


    /**
     * criar
     * apagar
     * retornar interesse individual
     */
}