
package api;

import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.LoginDto;
import dto.PostLoginDto;
import service.SessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Resource class for managing session-related operations.
 * Provides endpoints for login, logout, token renewal, and logging out from all devices.
 */
@Path("/sessions")
public class SessionResource {

    @EJB
    SessionService sessionService;

    private static final Logger LOGGER = LogManager.getLogger(SessionResource.class);

    /**
     * Renews the session token, extending its timeout.
     *
     * @param token the current session token
     * @return a Response indicating success or failure of the token renewal
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response renewToken(@HeaderParam("token") String token) {
        if (sessionService.incrementSessionTimeout(token)) {
            return Response.status(200).entity("Token timeout reset").build();
        } else {
            return Response.status(401).entity("Invalid token").build();
        }
    }

    /**
     * Performs user login, creating a new session if successful.
     *
     * @param loginDto the login data transfer object containing user credentials
     * @param request  the HTTP servlet request to obtain the client's IP address
     * @return a Response containing the session token if login is successful, or an error message if it fails
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginDto, @Context HttpServletRequest request) {
        String clientIP = request.getRemoteAddr();
        String token = sessionService.login(loginDto.getEmail(), loginDto.getPassword());

        if (token != null) {
            PostLoginDto postLoginDto = new PostLoginDto(token, sessionService.getSessionTimeout());
            LOGGER.info("Successful login from IP: " + clientIP + " for user: " + loginDto.getEmail());
            return Response.status(Response.Status.CREATED).entity(postLoginDto).build();
        } else {
            LOGGER.warn("Failed login attempt from IP: " + clientIP + " for user: " + loginDto.getEmail());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Login Failed").build();
        }
    }

    /**
     * Logs out the current session identified by the provided token.
     *
     * @param token the session token to be invalidated
     * @return a Response indicating success or failure of the logout operation
     */
    @DELETE
    public Response logout(@HeaderParam("token") String token) {
        if (sessionService.logout(token)) {
            return Response.status(Response.Status.OK).entity("Logout successful").build();
        } else {
            return Response.status(401).entity("Invalid token").build();
        }
    }

    /**
     * Logs out from all devices, invalidating all session tokens associated with the user.
     *
     * @param token the session token identifying the user
     * @return a Response indicating success or failure of the logout operation from all devices
     */
    @DELETE
    @Path("/all")
    public Response logoutAllDevices(@HeaderParam("token") String token) {
        if (sessionService.logoutAllDevices(token)) {
            return Response.status(200).entity("Logout from all devices successful").build();
        } else {
            return Response.status(401).entity("Invalid token").build();
        }
    }
}



/*
package api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.LoginDto;
import dto.PostLoginDto;
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

        String token = sessionService.login(loginDto.getEmail(), loginDto.getPassword());
        int timeout = sessionService.getSessionTimeout();
        if (token != null) {
            PostLoginDto postLoginDto = new PostLoginDto(token, ;
            return Response.status(200).entity().build();
        }
        return Response.status(401).entity("Login Failed").build();
    }

    */
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
    }*//*





    // logout
    @DELETE
    public Response logout(@HeaderParam("token") String token) {
        if (sessionService.logout(token)){
            return Response.status(200).entity("Logout successful").build();
        }
        else return Response.status(401).entity("Invalid token").build();
    }





    // para fazer login, logout, logoutAllDevices, active, gerar token, reset do timeout do token
}
*/
