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

// COMENTÀRIOS : USOS DESTE FICHEIRO
// recursos de utilizador
// utilizado para registar utilizadores, obter informação para perfil de utilizadores
// APAGAR UTILIZADORES (permite o próprio utilizador apagar a sua conta)

// fazer validação de DTOs pós endpoints de utilizador (validar se o email é válido, se a password é válida, etc)

    // POST /users: Registo de utilizador.
    // DELETE /users: Apagar utilizador.
    // get /users: obter dados do proprio utilizador. FALTA
    // get /users/{id}: obter dados de um utilizador. (se o perfil for publico) FALTA

    // campos depois para atualização, usar só /users/ sem id porque só quem chama pode manipular a si próprio

    // o caso especial da foto de perfil: neste ou noutro endpoint?



@Path("/users")
public class UserResource {

    @EJB
    private UserService userService;
    @EJB private SessionService sessionService;

    // PERMITE O REGISTO
    // vamos considerar que DTO é válido para já

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDto userDto) {
        if ((userService.registerUser(userDto)) == UserState.CREATED)
            return Response.status(201).entity("User created. Check your email "+userDto.getEmail()+" to activate").build();
        else return Response.status(400).entity("User already exists").build();

}

    @Path("/activations/{emailtoken}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyAccount(@PathParam("emailtoken") String emailtoken) {
        if ((userService.confirmUser(emailtoken))) {
            return Response.status(200).entity("ativado").build();
        }
        return Response.status(400).entity("Invalid email token").build();
    }

    // pedir para criar nova passe
    // chamado por "https://localhost:3000/forget-password/"
    @Path("/passwords/resets")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestActivation(@HeaderParam("email") String email) {
        if ((userService.requestPasswordReset(email))) {
            return Response.status(200).entity("Password reset email sent").build();
        }
        // ou request inválido pq pediu de pass. verificar cena de token expirado.
        return Response.status(400).entity("Invalid email").build();
    }

    //nova password
    @Path("/passwords/resets/{emailtoken}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response resetPassword(@PathParam("emailtoken") String emailtoken, String newPassword) {
        JsonObject jsonObject = Json.createReader(new StringReader(newPassword)).readObject();
        String password = jsonObject.getString("password");
        if ((userService.resetPassword(emailtoken, password))) {

            return Response.status(200).entity("Password reset").build();
        }
        return Response.status(400).entity("Invalid email token").build();
    }

    //o postlogin DTO retorna o user
    @Path("/{token}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("token") String token){
        //mecanismos de segurança aqui dentro
        if (sessionService.isTokenValid(token)) {
            return Response.status(200).entity(userService.getUserByToken(token)).build();
        }
        return Response.status(400).entity("user not found").build();
    }
    //get all users
    @Path("/available")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@HeaderParam("token") String token){
        if (sessionService.isTokenValid(token)){
            return Response.status(Response.Status.OK).entity(userService.membersAvailableProjects()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("invalid token").build();
    }
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
        return Response.status(400).entity("Token not valid").build();
    }
    @Path("/{userID}/addSkill/{skillID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSkillToUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("skillID") int skillID){
        if (sessionService.isTokenValid(token)){
            userService.addSkillToUser(userID, skillID);
            return Response.status(200).entity("skill was added to user").build();
        }
        return Response.status(400).entity("invalid token").build();
    }
    @Path("/{userID}/addInterest/{interestID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInterestToUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("interestID") int interestID){
        if (sessionService.isTokenValid(token)){
            userService.addInterestToUser(userID, interestID);
            return Response.status(200).entity("Interest was added to user").build();
        }
        return Response.status(400).entity("invalid token").build();
    }

    //remover skills e interesses
    @Path("/{userID}/removeSkill/{skillID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeSkillFromUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("skillID") int skillID){
        if (sessionService.isTokenValid(token)){
            userService.removeSkillFromUser(userID, skillID);
            return Response.status(200).entity("skill was removed to user").build();
        }
        return Response.status(400).entity("invalid token").build();
    }
    @Path("/{userID}/removeInterest/{skillID}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeInterestFromUser(@HeaderParam("token") String token, @PathParam("userID") int userID, @PathParam("skillID") int skillID){
        if (sessionService.isTokenValid(token)){
            userService.removeInterestFromUser(userID, skillID);
            return Response.status(200).entity("interest was removed to user").build();
        }
        return Response.status(400).entity("invalid token").build();
    }
    @Path("{userID}/edit")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(@HeaderParam("token") String token,@PathParam("userID") int userID, UserProfileDto user){
        if (sessionService.isTokenValid(token)){
            userService.editProfile(userID,user);
            return Response.status(200).entity("user was sucessfully updated!").build();
        }
        return Response.status(400).entity("invalid token").build();
    }


}

