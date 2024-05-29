package service;


import dao.UserDao;
import dao.SystemVariableDao;
import dao.WorkplaceDao;
import dto.UserDto;
import entity.UserEntity;
import enums.UserState;
import org.mindrot.jbcrypt.BCrypt;
import util.EmailSender;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime;
import java.util.UUID;

@Stateless
public class UserService {

    @EJB
    private UserDao userDao;

    @EJB
    private SystemVariableDao systemVariableDao;

    @EJB
    private WorkplaceDao workplaceDao;


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
        String verificationLink = "https://localhost:8443/activations/" + user.getEmailToken();
        EmailSender.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationLink);
        userDao.persist(user);
         return UserState.CREATED;

    }

    // função que vai ativar user
    //

    public boolean activateUser(String emailToken) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
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
        String resetLink = "http://localhost:3000/reset-password/" + user.getEmailToken();
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



    // converter userDto em entity e vice versa
    // rever

}
