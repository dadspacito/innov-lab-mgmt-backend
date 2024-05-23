package service;

import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import enums.AuthState;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime;


@Stateless

public class AuthService {

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

    // criar as funlões de buscar user por token, email etc...

    // falta as de login.
    // falta incrementar tempo de token

// falta o active
    // falta gerar o token

    // incrementar token.




    // LOGIN



    public String login(String email, String password) {
        // verificar se o utilizador existe
        // verificar se a password está correta
        // gerar token de sessão
        // guardar token de sessão na base de dados
        // devolver token de sessão

        return null;
    }

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

    public AuthState checkTokenState(String token) {

    sessionTokenDao.findUserBySessionToken(token);


    }





    public boolean incrementSessionTimeout(String token) {


        // verificações aqui ou na camada rest?
        // verificar se o token é válido
        // buscar
        // devolver true se o token foi incrementado
        // devolver false se o token não foi incrementado

        int timeout = systemVariableDao.findSystemVariableByName("session_timeout").getVariableValue();
        LocalDateTime newtimeout = LocalDateTime.now().plusMinutes(timeout);


        //   sessionTokenDao.updateTokenSessionTimeout(token, timeout )
        return sessionTokenDao.resetTokenSessionTimeout(token, newtimeout);
    }




}
