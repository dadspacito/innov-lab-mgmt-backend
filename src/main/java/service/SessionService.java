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
/**
 * Service class for handling session management, including login, logout, and session token creation.
 */
@Stateless
public class SessionService {
    @EJB
    private SessionTokenDao sessionTokenDao;
    @EJB
    private UserDao userDao;
    @EJB
    private SystemVariableDao systemVariableDao;
    private static final Logger LOGGER = LogManager.getLogger(SessionService.class);
    /**
     * Logs in a user by validating their email and password, and creates a session token if successful.
     *
     * @param email the email of the user.
     * @param password the password of the user.
     * @return the created session token if login is successful, null otherwise.
     */
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

    /**
     * Validates the provided password against the stored hashed password.
     *
     * @param password the plain text password.
     * @param hashedPassword the hashed password stored in the database.
     * @return true if the password is valid, false otherwise.
     */
    private boolean isPasswordValid(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    /**
     * Creates a session token for the specified user.
     *
     * @param userEntity the user entity for whom the token is being created.
     * @return the created session token.
     * @throws TokenCreationException if there is an error during token creation.
     */
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
    /**
     * Generates a unique session token.
     *
     * @return a unique session token.
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
    /**
     * Retrieves the session timeout value from the system variables.
     *
     * @return the session timeout value in minutes.
     */
    public int getSessionTimeout() {
        return systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
    }
    /**
     * Saves the session token entity to the database.
     *
     * @param userEntity the user entity for whom the token is being created.
     * @param token the generated session token.
     * @param timeout the timeout value for the session token.
     */
    private void saveSessionToken(UserEntity userEntity, String token, int timeout) {
        SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
        sessionTokenEntity.setToken(token);
        sessionTokenEntity.setUser(userEntity);
        sessionTokenEntity.setTokenTimeout(LocalDateTime.now().plusMinutes(timeout));
        sessionTokenDao.persist(sessionTokenEntity);
        sessionTokenDao.flush();
    }
    /**
     * Logs out a user by deleting their session token.
     *
     * @param token the session token to be deleted.
     * @return true if the logout is successful, false otherwise.
     */
    public boolean logout(String token) {
        try {
            sessionTokenDao.deleteSessionToken(token);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error during logout: ", e);
            return false;
        }
    }
    /**
     * Logs out a user from all devices by deleting all their session tokens.
     *
     * @param token the session token of the user.
     * @return true if the logout is successful, false otherwise.
     */
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
    /**
     * Increments the session timeout for the specified session token.
     *
     * @param token the session token whose timeout needs to be incremented.
     * @return true if the timeout increment is successful, false otherwise.
     */
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
    /**
     * Checks if the specified session token is valid.
     *
     * @param token the session token to be checked.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token) {
        SessionTokenEntity sessionTokenEntity = sessionTokenDao.findSessionTokenByToken(token);
        if (sessionTokenEntity != null && sessionTokenEntity.getTokenTimeout().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
}

