
package api;

import dto.InterestDto;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
}