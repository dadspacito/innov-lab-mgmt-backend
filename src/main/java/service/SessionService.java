package service;

import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import entity.SessionTokenEntity;
import entity.UserEntity;
import enums.AuthState;
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

    // criar as funlões de buscar user por token, email etc...

    // falta as de login.
    // falta incrementar tempo de token

// falta o active
    // falta gerar o token

    // incrementar token.

    // LOGIN



    // depois expressão de getToken.
    // só chamar isto depois de todas as verificações




    // expressão de classes sao privadas


    // rever

    public String login(String email, String password) {
        UserEntity user = userDao.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (BCrypt.checkpw(password, user.getPassword())) {
//            if (user.isConfirmed()) {
//                if (user.isActive()) {
//                    if (createToken(user)) {
//                        return sessionTokenDao.findSessionTokenByUser(user).getToken();
//                    }
//                }
//            }
        }
        return null;
    }


    public boolean createToken(UserEntity userEntity) {

        try {
            String token = UUID.randomUUID().toString();
            int timeout = systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
            SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
            sessionTokenEntity.setToken(token);
            sessionTokenEntity.setUser(userEntity);
            sessionTokenEntity.setTokenTimeout(LocalDateTime.now().plusMinutes(timeout));
            sessionTokenDao.persist(sessionTokenEntity);
            sessionTokenDao.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public AuthState checkAuthState(String email, String password) {
        // verificar se o utilizador existe
        // verificar se a password está correta
        // gerar token de sessão
        // guardar token de sessão na base de dados
        // devolver token de sessão




        return null;
    }

    // devolve states e só com states validos gera token!



    // password depois, partir pra testes





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


    // AUX functions


    // verificação do estado do token
/*
    public AuthState checkTokenState(String token) {

        UserEntity user = sessionTokenDao.findUserBySessionToken(token);

    if (user == null) {
        return AuthState.INVALID;
        }
    if (user.isConfirmed() == false) {
        return AuthState.MISSING;
        }



    }*/




// limpar a sessão do update


    public boolean incrementSessionTimeout(String token) {

        int timeout = systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
        LocalDateTime newtimeout = LocalDateTime.now().plusMinutes(timeout);

        return sessionTokenDao.resetTokenSessionTimeout(token, newtimeout);
    }




}
