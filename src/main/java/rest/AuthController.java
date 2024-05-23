package rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.LoginDto;
import enums.AuthState;
import service.AuthService;


@Path("/auth")
public class AuthController {

    @Inject
    AuthService authService;


    @Path("/renew")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response renewToken(@HeaderParam("token") String token) {
       // authService.incrementSessionTimeout(token);
        authService.incrementSessionTimeout(token);
        return Response.ok().build();
    }


    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginDto) {

        return Response.ok().build();
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
