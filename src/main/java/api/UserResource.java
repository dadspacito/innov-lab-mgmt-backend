package api;

import dto.UserDto;
import dto.UserProfileDto;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.SessionService;
import service.UserService;
import enums.UserState;

import java.io.StringReader;
import java.net.URL;
/**
 * UserResource class provides RESTful endpoints for user management.
 */
@Path("/users")
public class UserResource {

    @EJB
    private UserService userService;
    @EJB private SessionService sessionService;
    /**
     * Registers a new user.
     *
     * @param userDto the user DTO
     * @return the response indicating the result of the registration
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDto userDto) {
        if ((userService.registerUser(userDto)) == UserState.CREATED) {
            return Response.status(201).entity("User created. Check your email " + userDto.getEmail() + " to activate").build();
        }
        else return Response.status(400).entity("User already exists").build();
    }
    /**
     * Verifies a user account using an email token.
     *
     * @param emailtoken the email token
     * @return the response indicating the result of the verification
     */
    @Path("/activations/{emailtoken}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyAccount(@PathParam("emailtoken") String emailtoken) {
        if ((userService.confirmUser(emailtoken))) {
            return Response.status(200).entity("ativado").build();
        }
        else return Response.status(400).entity("Invalid email token").build();
    }
    /**
     * Requests a password reset.
     *
     * @param email the user's email
     * @return the response indicating the result of the password reset request
     */
    @Path("/passwords/resets")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestActivation(@HeaderParam("email") String email) {
        if ((userService.requestPasswordReset(email))) {
            return Response.status(200).entity("Password reset email sent").build();
        }
        else return Response.status(400).entity("Invalid email").build();
    }
    /**
     * Resets a user's password using an email token.
     *
     * @param emailtoken the email token
     * @param newPassword the new password
     * @return the response indicating the result of the password reset
     */
    @Path("/passwords/resets/{emailtoken}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response resetPassword(@PathParam("emailtoken") String emailtoken, String newPassword) {
        JsonObject jsonObject = Json.createReader(new StringReader(newPassword)).readObject();
        String password = jsonObject.getString("password");
        if ((userService.resetPassword(emailtoken, password))) {
            return Response.status(200).entity("Password reset").build();
        }
        else return Response.status(400).entity("Invalid email token").build();
    }
    /**
     * Retrieves a user by session token.
     *
     * @param token the session token
     * @return the response containing the user DTO
     */
    @Path("/{token}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("token") String token){
        if (sessionService.isTokenValid(token)) {
            return Response.status(200).entity(userService.getUserByToken(token)).build();
        }
        else return Response.status(400).entity("user not found").build();
    }
    /**
     * Retrieves all users available for projects.
     *
     * @param token the session token
     * @return the response containing the list of available users
     */
    @Path("/available")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@HeaderParam("token") String token){
        if (sessionService.isTokenValid(token)){
            return Response.status(Response.Status.OK).entity(userService.membersAvailableProjects()).build();
        }
        else return Response.status(Response.Status.UNAUTHORIZED).entity("invalid token").build();
    }
    /**
     * Retrieves the profile of a user.
     *
     * @param token the session token
     * @param userID the user ID
     * @return the response containing the user profile DTO
     */
    @Path("/profile/{userID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@HeaderParam("token") String token, @PathParam("userID") int userID){
        if (sessionService.isTokenValid(token)){
            if(userService.getUserByID(userID).isPublicProfile()) {
                return Response.status(200).entity(userService.getUserProfileDto(userID)).build();
            }
            else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User profile is private").build();
            }
        }
        else return Response.status(400).entity("Token not valid").build();
    }
    /**
     * Adds a skill to a user.
     *
     * @param token the session token
     * @param userID the user ID
     * @param skillID the skill ID
     * @return the response indicating the result of the operation
     */
    @Path("/{userID}/addSkill/{skillID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSkillToUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("skillID") int skillID){
        if (sessionService.isTokenValid(token)){
            userService.addSkillToUser(userID, skillID);
            return Response.status(200).entity("skill was added to user").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Adds an interest to a user.
     *
     * @param token the session token
     * @param userID the user ID
     * @param interestID the interest ID
     * @return the response indicating the result of the operation
     */
    @Path("/{userID}/addInterest/{interestID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInterestToUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("interestID") int interestID){
        if (sessionService.isTokenValid(token)){
            userService.addInterestToUser(userID, interestID);
            return Response.status(200).entity("Interest was added to user").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Removes a skill from a user.
     *
     * @param token the session token
     * @param userID the user ID
     * @param skillID the skill ID
     * @return the response indicating the result of the operation
     */
    @Path("/{userID}/removeSkill/{skillID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeSkillFromUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("skillID") int skillID){
        if (sessionService.isTokenValid(token)){
            userService.removeSkillFromUser(userID, skillID);
            return Response.status(200).entity("skill was removed to user").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Removes an interest from a user.
     *
     * @param token the session token
     * @param userID the user ID
     * @param interestID the interest ID
     * @return the response indicating the result of the operation
     */
    @Path("/{userID}/removeInterest/{interestID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeInterestFromUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("interestID") int interestID){
        if (sessionService.isTokenValid(token)){
            userService.removeInterestFromUser(userID, interestID);
            return Response.status(200).entity("interest was removed to user").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Edits a user's profile.
     *
     * @param token the session token
     * @param userID the user ID
     * @param user the user profile DTO
     * @return the response indicating the result of the operation
     */
    @Path("{userID}/edit")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(@HeaderParam("token") String token,@PathParam("userID") int userID, UserProfileDto user){
        if (sessionService.isTokenValid(token)){
            userService.editProfile(userID,user);
            return Response.status(200).entity("user was sucessfully updated!").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Removes a user from a project.
     *
     * @param token the session token
     * @param userID the user ID
     * @param projectID the project ID
     * @return the response indicating the result of the operation
     */
    @Path("/{userID}/leaveProject/{projectID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response leaveProject(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("projectID") int projectID){
        if (sessionService.isTokenValid(token)){
            userService.leaveProject(userID, projectID);
            return Response.status(200).entity("user has successfully left the project").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Adds a user to a project.
     *
     * @param token the session token
     * @param userID the user ID
     * @param projectID the project ID
     * @return the response indicating the result of the operation
     */
    @Path("/{userID}/joinProject/{projectID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response joinProject(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("projectID") int projectID){
        if (sessionService.isTokenValid(token)){
            userService.joinProject(userID, projectID);
            return Response.status(200).entity("user has successfully joined the project").build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
    /**
     * Retrieves the projects of a user.
     *
     * @param token the session token
     * @param userID the user ID
     * @return the response containing the list of user projects
     */
    @Path("/getProjects/{userID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProjects(@HeaderParam("token") String token, @PathParam("userID") int userID){
        if (sessionService.isTokenValid(token)){
            return Response.status(200).entity(userService.getUserProjects(userID)).build();
        }
        else return Response.status(400).entity("invalid token").build();
    }
}

