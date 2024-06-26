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
import org.apache.logging.log4j.*;
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



    private static final Logger LOGGER = LogManager.getLogger(SessionService.class);

    // LOGIN
    public String login(String email, String password) {
        try {
            UserEntity user = userDao.findUserByEmail(email);
            if (user != null && isPasswordValid(password, user.getPassword()) && user.isConfirmed() && user.isActive()) {
                LOGGER.info("User " + email + " logged in successfully");
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
            // teste se log funciona, faz log da açao
            LOGGER.info("Token created for user: " + userEntity.getEmail());
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

    // verificar se token está válido

    public boolean isTokenValid(String token) {

        SessionTokenEntity sessionTokenEntity = sessionTokenDao.findSessionTokenByToken(token);
        if (sessionTokenEntity != null && sessionTokenEntity.getTokenTimeout().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }


}

