
package api;

import dao.SessionTokenDao;
import entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.LoginDto;
import dto.PostLoginDto;
import service.SessionService;
import org.apache.logging.log4j.*;
import service.UserService;

/**
 * Resource class for managing session-related operations.
 * Provides endpoints for login, logout, token renewal, and logging out from all devices.
 */
@Path("/sessions")
public class SessionResource {

    @EJB
    private SessionService sessionService;
    @EJB
    private SessionTokenDao sessionTokenDao;
    @EJB private UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(SessionResource.class);
    /**
     * Performs user login, creating a new session if successful.
     *
     * @param loginDto the login data transfer object containing user credentials
     * @param request  the HTTP servlet request to obtain the client's IP address
     * @return a Response containing the session token and session timeout if login is successful, or an error message if it fails
     *
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginDto, @Context HttpServletRequest request) {
        String clientIP = request.getRemoteAddr();
        String token = sessionService.login(loginDto.getEmail(), loginDto.getPassword());
        if (token != null) {
            UserEntity ue = sessionTokenDao.findUserBySessionToken(token);
            PostLoginDto postLoginDto = new PostLoginDto(ue, token, sessionService.getSessionTimeout());
            LOGGER.info("Successful login from IP: " + clientIP + " for user: " + loginDto.getEmail());
            return Response.status(Response.Status.CREATED).entity(postLoginDto).build();
        } else {
            LOGGER.warn("Failed login attempt from IP: {} for user: {}", clientIP, loginDto.getEmail());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Login Failed").build();
        }
    }

    /**
     * Renews the session token, extending its timeout.
     *
     * @param token the current session token
     * @return a Response indicating success or failure of the token renewal
     */

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response renewToken(@HeaderParam("token") String token) {
        if (sessionService.isTokenValid(token)) {
            sessionService.incrementSessionTimeout(token);
            LOGGER.error("Token successfully renewed for" + userService.getUserByToken(token).getEmail());
            return Response.status(Response.Status.OK).entity("Token timeout reset").build();
        } else {
            LOGGER.error("Error renewing token for " + userService.getUserByToken(token).getEmail());
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid token").build();

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
        if (sessionService.isTokenValid(token) ) {
            sessionService.logout(token);
            LOGGER.warn("User logged out" + userService.getUserByToken(token).getEmail());
            return Response.status(Response.Status.OK).entity("Logout successful").build();
        } else {
            LOGGER.error("Error logging out " +  userService.getUserByToken(token).getEmail());
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
        if (sessionService.isTokenValid(token)) {
            sessionService.logoutAllDevices(token);
            LOGGER.warn("Logged all accounts " + userService.getUserByToken(token).getEmail());
            return Response.status(200).entity("Logout from all devices successful").build();
        } else {
            LOGGER.error("Error logging out on all devices " + userService.getUserByToken(token).getEmail());
            return Response.status(401).entity("Invalid token").build();
        }
    }
}
