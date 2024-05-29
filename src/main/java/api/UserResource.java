package api;

import dto.UserDto;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.UserService;
import enums.UserState;

// COMENTÀRIOS : USOS DESTE FICHEIRO
// recursos de utilizador
// utilizado para registar utilizadores, obter informação para perfil de utilizadores
// APAGAR UTILIZADORES (permite o próprio utilizador apagar a sua conta)

// fazer validação de DTOs pós endpoints de utilizador (validar se o email é válido, se a password é válida, etc)

    // POST /users: Registo de utilizador.
    // DELETE /users: Apagar utilizador.
    // get /users: obter dados do proprio utilizador.
    // get /users/{id}: obter dados de um utilizador. (se o perfil for publico)

    // campos depois para atualização, usar só /users/ sem id porque só quem chama pode manipular a si próprio

    // o caso especial da foto de perfil: neste ou noutro endpoint?



@Path("/users")
public class UserResource {

    @EJB
    private UserService userService;

    // PERMITE O REGISTO
    // vamos considerar que DTO é válido para já

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDto userDto) {
        if ((userService.registerUser(userDto)) == UserState.CREATED)
            return Response.status(201).entity("yey").build();
        else return Response.status(400).entity("User already exists").build();

}

// acrescentar endpoint de foto de perfil

    // endpoint para ativar a conta
    // verificar siuação de quando já foi feito set.. ( n faz mal pq função só ativa e não desativa)



    @Path("/activations/{emailtoken}")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyAccount(@PathParam("emailtoken") String emailtoken) {
        if ((userService.activateUser(emailtoken))) {
            return Response.status(200).entity("ativado").build();
        }
        return Response.status(400).entity("Invalid email token").build();
    }

    // pedir para criar nova passe
    // chamado por "http://localhost:3000/forget-password/"
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



    // endpoint para fazer reset password
    // criar nova passe
    // chamado por "http://localhost:3000/reset-password/"

    @Path("/passwords/resets/{emailtoken}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response resetPassword(@PathParam("emailtoken") String emailtoken, String password) {
        if ((userService.resetPassword(emailtoken, password))) {
            return Response.status(200).entity("Password reset").build();
        }
        return Response.status(400).entity("Invalid email token").build();
    }






    // verify (confirm) account


    //

    // PATCH /activations/{emailtoken}: Verificação de conta.






}

