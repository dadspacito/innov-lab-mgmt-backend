package api;

import entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.HeaderDto;
import service.SessionService;
import service.UserService;
import org.apache.logging.log4j.*;


@Path("/header")
public class HeaderResource {
        @EJB
        SessionService sessionService;
        @EJB
        UserService userService;
        private static final Logger LOGGER = LogManager.getLogger(HeaderResource.class);
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response getHeader(@HeaderParam("token") String token) {

            if (sessionService.isTokenValid(token)) {
                UserEntity user = userService.getUserByToken(token);
                HeaderDto headerDto = userService.getHeader(user);

                return Response.status(Response.Status.OK).entity(headerDto).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid token").build();
            }
        }


}
