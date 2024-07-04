package api;


import dto.MaterialDto;
import jakarta.ejb.EJB;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.MaterialService;
import service.SessionService;
import service.UserService;

@Path("/materials")
public class MaterialResource {

    @EJB
    private MaterialService materialService;
    @EJB private UserService userService;
    @EJB private SessionService sessionService;
    //este path pede o token como header para verificar que o user existe
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMaterials (@HeaderParam("token") String token){
        if (userService.getUserByToken(token)!= null){
            return Response.status(200).entity(materialService.getAllMaterials()).build();
        }
        return Response.status(400).entity("Materials were not returned").build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMaterial (@HeaderParam("token") String token, MaterialDto material){
        if (userService.getUserByToken(token)!= null){
            //perguntar como se verifica a validade disto
            materialService.addMaterialToDB(material);
            return Response.status(201).entity("material created").build();
        }
        return Response.status(400).entity("material was not created").build();
    }
    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeMaterial(@HeaderParam("token") String token, @PathParam("id") int id){
        if(sessionService.isTokenValid(token)){
            try {
                materialService.removeMaterialFromDB(id);
                return Response.status(200).entity("Material was removed from db").build();
            }
            catch (NoResultException e){
                System.err.println("Material was not removed");
                return Response.status(400).entity("Material was not removed").build();

            }
        }
        return Response.status(401).entity("Token is not valid").build();
    }
}
