package api;


import dto.MaterialsDto;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.MaterialsService;
import service.SessionService;
import service.UserService;

@Path("/materials")
public class MaterialsResource {

    @EJB
    private MaterialsService materialsService;
    @EJB private UserService userService;
    //este path pede o token como header para verificar que o user existe
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMaterials (@HeaderParam("token") String token){
        if (userService.getUserByToken(token)!= null){
            return Response.status(200).entity(materialsService.getAllMaterials()).build();
        }
        return Response.status(400).entity("Materials were not returned").build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMaterial (@HeaderParam("token") String token, MaterialsDto material){
        if (userService.getUserByToken(token)!= null){
            //perguntar como se verifica a validade disto
            materialsService.addMaterialToDB(material);
            return Response.status(201).entity("material created").build();
        }
        return Response.status(400).entity("material was not created").build();
    }
}
