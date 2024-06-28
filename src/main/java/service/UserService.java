package service;


import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import dao.WorkplaceDao;

import dto.HeaderDto;

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
     //   user.setCreatedAt(LocalDateTime.now());
        user.setActive(false);
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

    public boolean confirmUser(String emailToken) {
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

    // obter user pelo token (IMPORTANTE. função só chamada EM RESOURCE após validação de token, logo user existe)
    public UserEntity getUserByToken(String token) {
        return sessionTokenDao.findUserBySessionToken(token);
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


    //função que envia mail ao user para seguir para o link de reset password
    public boolean requestPasswordReset(String email) {
        UserEntity user = userDao.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        user.setEmailToken(generateNewToken());
        user.setEmailTokenExpires(LocalDateTime.now().plusHours(1));
        userDao.merge(user);
        String resetLink = "http://localhost:3000/reset-password/" + user.getEmailToken();
        EmailSender.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetLink);
        return true;
    }

    public boolean resetPassword(String emailToken, String newPassword) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
            return false;
        }
        System.out.println("entrou aqui");
        System.out.println(newPassword);
        System.out.println(user.getPassword());
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        System.out.println(user.getPassword());
        user.setEmailToken(null);
        user.setEmailTokenExpires(null);
        userDao.merge(user);
        System.out.println(user.getPassword());
        return true;
    }



    public HeaderDto getHeader (UserEntity user) {
        HeaderDto headerDto = new HeaderDto();
        if (user.getNickname() != null) {
            headerDto.setNickname(user.getNickname());
        } else {
            headerDto.setNickname(user.getFirstName()   + " " + user.getLastName());
        }
        headerDto.setAvatar(user.getAvatar());
        return headerDto;
    }



   //chama o edit user
    //recebe os dados de user do frontend e faz merge
    


}
