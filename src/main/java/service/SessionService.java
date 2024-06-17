package service;

import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import entity.SessionTokenEntity;
import entity.UserEntity;
import exception.TokenCreationException;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDateTime;
import java.util.UUID;

@Stateless
public class SessionService {

    // COMENTÁRIO
    // Classe para gerenciar serviços de sessão, usada para:
    // - Gerar tokens de sessão
    // - Verificar se os tokens de sessão são válidos
    // - Apagar tokens de sessão
    // - Apagar todos os tokens de sessão de um user
    // - Reset do timeout do token

    // - Apagar todos os tokens de sessão inválidos (com session timeout expirado) (isto vai ser no scheduler)

    @EJB
    private SessionTokenDao sessionTokenDao;

    @EJB
    private UserDao userDao;

    @EJB
    private SystemVariableDao systemVariableDao;

    @EJB
    private UserService userService;

    private static final Logger LOGGER = LogManager.getLogger(SessionService.class);

    // LOGIN
    public String login(String email, String password) {
        try {
            UserEntity user = userDao.findUserByEmail(email);
            if (user != null && isPasswordValid(password, user.getPassword()) && user.isConfirmed() && !user.isActive()) {
                return createToken(user);
            }
        } catch (Exception e) {
            LOGGER.error("Error during login: ", e);
        }
        return null;
    }

    private boolean isPasswordValid(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Transactional
    public String createToken(UserEntity userEntity) {
        try {
            String token = generateToken();
            int timeout = getSessionTimeout();
            saveSessionToken(userEntity, token, timeout);
            return token;
        } catch (Exception e) {
            LOGGER.error("Error creating token: ", e);
            throw new TokenCreationException("Failed to create session token", e);
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public int getSessionTimeout() {
        return systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
    }
 //systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();


    private void saveSessionToken(UserEntity userEntity, String token, int timeout) {
        SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
        sessionTokenEntity.setToken(token);
        sessionTokenEntity.setUser(userEntity);
        sessionTokenEntity.setTokenTimeout(LocalDateTime.now().plusMinutes(timeout));
        sessionTokenDao.persist(sessionTokenEntity);
        sessionTokenDao.flush();
    }

    // LOGOUT
    public boolean logout(String token) {
        try {
            sessionTokenDao.deleteSessionToken(token);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error during logout: ", e);
            return false;
        }
    }

    public boolean logoutAllDevices(String token) {
        try {
            UserEntity user = sessionTokenDao.findUserBySessionToken(token);
            sessionTokenDao.deleteAllSessionTokens(user);
            sessionTokenDao.flush();
            return true;
        } catch (Exception e) {
            LOGGER.error("Error during logout from all devices: ", e);
            return false;
        }
    }

    // Reset timeout do token
    public boolean incrementSessionTimeout(String token) {
        try {
            int timeout = getSessionTimeout();
            LocalDateTime newTimeout = LocalDateTime.now().plusMinutes(timeout);
            sessionTokenDao.resetTokenSessionTimeout(token, newTimeout);
            sessionTokenDao.flush();
            return true;
        } catch (Exception e) {
            LOGGER.error("Error incrementing session timeout: ", e);
            return false;
        }
    }
}



/*
package service;

import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import entity.SessionTokenEntity;
import entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.UUID;


@Stateless

public class SessionService {

    // COMENTÁRIO
    // classe gerar serviços de sessao, vai ser usada para gerar tokens de sessao
    // e verificar se os tokens de sessao sao validos
    // e apagar tokens de sessao
    // e apagar todos os tokens de sessao de um utilizador
    // e apagar todos os tokens de sessao inválidos (com session timeout caducado)
    // reset do timeout do token .

    @EJB
    private SessionTokenDao sessionTokenDao;

    @EJB
    private UserDao userDao;

    @EJB
    private SystemVariableDao systemVariableDao;

    @EJB
    private UserService userService;



    // LOGIN







    // expressão de classes sao privadas





    // login, buscar entidade por email, verificar se a password é correta, verificar se o utilizador está confirmado, verificar se o utilizador está ativo, criar token de sessão, guardar token de sessão na base de dados, devolver token de sessão

    public String login(String email, String password) {
        UserEntity user = userDao.findUserByEmail(email);
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                if (user.isConfirmed()) {
                    if (!user.isDeleted()) {
                        return createToken(user);
                        }
                    }
                }return null;
            } return null;

    }

    public String createToken(UserEntity userEntity) {

        try {
            String token = UUID.randomUUID().toString();
            int timeout = systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
            SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
            sessionTokenEntity.setToken(token);
            sessionTokenEntity.setUser(userEntity);
            sessionTokenEntity.setTokenTimeout(LocalDateTime.now().plusMinutes(timeout));
            sessionTokenDao.persist(sessionTokenEntity);
            sessionTokenDao.flush();
            return token;
        } catch (Exception e) {
            return "ERROR";
        }
    }







        // verificar se o utilizador existe



        // verificar se a password está correta
        // gerar token de sessão
        // guardar token de sessão na base de dados
        // devolver token de sessão



    // LOGOUT
    public boolean logout(String token) {
        sessionTokenDao.deleteSessionToken(token);

        return true;
    }

    public boolean logoutAllDevices(String token) {
        sessionTokenDao.deleteAllSessionTokens(sessionTokenDao.findUserBySessionToken(token));
        return true;
    }





// limpar a sessão do update


    public boolean incrementSessionTimeout(String token) {

        int timeout = systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
        LocalDateTime newtimeout = LocalDateTime.now().plusMinutes(timeout);

        return sessionTokenDao.resetTokenSessionTimeout(token, newtimeout);
    }




}
*/
