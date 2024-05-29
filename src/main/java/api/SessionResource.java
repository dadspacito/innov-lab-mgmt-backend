package api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.LoginDto;
import service.SessionService;


// recurso são as sessões
// como os endpoints delete só podem ser acedidos com token, só o próprio user pode apagar a sessão

//POST /sessions: Login (criação de uma nova sessão).
//PATCH /sessions: Renovação da sessão
//DELETE /sessions: Logout (apagar sessão atual).
// DELETE /sessions/all : Logout em todos os dispositivos (apagar todas as sessões).




@Path("/sessions")
public class SessionResource {

    @Inject
    SessionService sessionService;


    // renew session token
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response renewToken(@HeaderParam("token") String token) {

        if (sessionService.incrementSessionTimeout(token)){
            return Response.status(200).entity("Token timeout reset").build();
        }
        else return Response.status(401).entity("Invalid token").build();
    }

    // login
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginDto) {
        // consome login dto, produz postlogin
        // devolve token e timeout


    }

    // logout
    @DELETE
    public Response logout(@HeaderParam("token") String token) {
        if (sessionService.logout(token)){
            return Response.status(200).entity("Logout successful").build();
        }
        else return Response.status(401).entity("Invalid token").build();
    }



/*    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        String token = userBean.login(username, password);
        if (token != null) {
            if(userBean.getUserByUsername(username).isActive()){
                return Response.status(200).entity(JsonUtils.convertObjectToJson(new TokenAndRoleDto(token, userBean.getUserByUsername(username).getRole(), userBean.getUserByToken(token).getUsername()))).build();
            }else{
                return Response.status(403).entity(JsonUtils.convertObjectToJson(new ResponseMessage("User is not active")).toString()).build();
            }
        }
        return Response.status(401).entity(JsonUtils.convertObjectToJson(new ResponseMessage("Login Failed"))).build();
    }*/




    // para fazer login, logout, logoutAllDevices, active, gerar token, reset do timeout do token
}
