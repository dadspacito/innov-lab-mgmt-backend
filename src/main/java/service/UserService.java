package service;


import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import dao.WorkplaceDao;
import dto.PostLoginDto;
import dto.UserDto;
import entity.UserEntity;
import enums.UserState;
import org.mindrot.jbcrypt.BCrypt;
import util.EmailSender;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Stateless
public class UserService {

    @EJB
    private UserDao userDao;

    @EJB
    private SystemVariableDao systemVariableDao;

    @EJB
    private WorkplaceDao workplaceDao;
    @EJB private SessionTokenDao sessionTokenDao;


// função que vai adicionar user
    // recebe user dto
    // DTO vai ser válido quando chega aqui
    // incluir a validação das entidades associadas. Se workplace não existir, retornar NOT_FOUND

    public UserState registerUser(UserDto userDto) {

        if (userDao.findUserByEmail(userDto.getEmail()) != null) {
            return UserState.ALREADY_EXISTS;
        }

        UserEntity user = new UserEntity();
        // criar user entity com dados de dto
        // associar dados do DTO ao user
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        user.setAvatar("https://commons.wikimedia.org/wiki/File:Default_avatar_profile.jpg");
        user.setWorkplace(workplaceDao.find(userDto.getWorkplaceId()));
        // a verificação ja foi feita, nao vai dar erro.
        user.setCreatedAt(LocalDateTime.now());
        user.setDeleted(false);
        user.setConfirmed(false);
        user.setEmailToken(generateNewToken());
        user.setEmailTokenExpires(LocalDateTime.now().plusHours(1));
        String verificationLink = "http://localhost:3000/users/activations/" + user.getEmailToken();
        EmailSender.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationLink);
        userDao.persist(user);
         return UserState.CREATED;

    }

    // função que vai ativar user
    //

    public boolean activateUser(String emailToken) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
            System.out.println("entrei no activate user no user service");

            return false;
        }
        user.setConfirmed(true);
        user.setEmailToken(null);
        user.setEmailTokenExpires(null);
        userDao.merge(user);
        return true;
    }

    public UserState checkUserState (String email) {

        UserEntity user = userDao.findUserByEmail(email);
        /// retornar por cada tipo de condição
        if (user == null) {
            return UserState.NOT_FOUND;
        }
        if (!user.isConfirmed()) {
            return UserState.NOT_CONFIRMED;
        }
       /* if (user.isActive() == false) {
            return UserState.DELETED;
        }*/
        return UserState.ACTIVE;
    }

    // métodos privados, de classe

    // gerar token para email

    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }


    public boolean requestPasswordReset(String email) {
        UserEntity user = userDao.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        user.setEmailToken(generateNewToken());
        user.setEmailTokenExpires(LocalDateTime.now().plusHours(1));
        userDao.merge(user);
        String resetLink = "https://localhost:3000/reset-password/" + user.getEmailToken();
        EmailSender.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetLink);
        return true;
    }

    public boolean resetPassword(String emailToken, String newPassword) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
            return false;
        }
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setEmailToken(null);
        user.setEmailTokenExpires(null);
        userDao.merge(user);
        return true;
    }




    //recebe user entity e retorna um user DTO
    //aqui tem de retornar o postLogin DTO para login
    //ha alguma altura em que seja necessário o user inteiro?
    //tem de receber um token
    private PostLoginDto convertUserEntityToUserDTO(UserEntity ue){
        PostLoginDto ud = new PostLoginDto();
        ud.setId(ue.getId());
        ud.setFirstName(ue.getFirstName());
        ud.setLastName(ue.getLastName());
        ud.setNickname(ue.getNickname());
        ud.setEmail(ue.getEmail());
        ud.setAvatar(ue.getAvatar());
        ud.setBio(ue.getBio());
        //ud.setAdmin(ue.isAdmin());
        //ud.setPassword(ue.getPassword());
        //ud.setPublicProfile(ue.isPublicProfile());
        //ud.setDeleted(ue.isDeleted());
        ud.setWorkplaceId(ue.getWorkplace().getId());
        return ud;
    }



    //19/06/2024
    //é preciso retornar users independentes e a lista de todos os users para o frontend
    //retornar user independente.GET USER BY EMAIL
    //aqui tem de ser por id para retornar o id do user que vem do token
    public PostLoginDto getUserByToken(String token){
        UserEntity ue = sessionTokenDao.findUserBySessionToken(token);
        if (ue != null){
            return convertUserEntityToUserDTO(ue) ;
        }
        return null;
    }
    //aqui tem de retornar as locatios todas




    //devolve sempre uma lista de users para só se ter um get no serviço
    //tem que percorrer uma lista e retornar uma lista com os users que se quer


}
