package api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.WorkplaceService;

@Path("/locations")
public class WorkplaceResource {
    @EJB
    private WorkplaceService workplaceService;




    //este endpoint precisa de ser mais verificado a nivel de integridade
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocations(){
        return Response.status(200).entity(workplaceService.returnLocations()).build();
    }

}
