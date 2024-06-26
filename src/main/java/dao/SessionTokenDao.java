package dao;

import entity.SessionTokenEntity;
import entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;

@Stateless
public class SessionTokenDao extends AbstractDao<SessionTokenEntity> {

    private static final long serialVersionUID = 1L;

    public SessionTokenDao() {
        super(SessionTokenEntity.class);
    }

    // COMENTÁRIO : obter utilizador a partir do token

    public UserEntity findUserBySessionToken(String token) {
        try {
            return em.createNamedQuery("SessionToken.findUserByToken", UserEntity.class).setParameter("token", token)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }



    // COMENTÁRIO : atualizar timeout do token



    public boolean resetTokenSessionTimeout (String token, LocalDateTime newsessiontimeout) {
        try {
            em.createNamedQuery("SessionToken.resetTokenSessionTimeout")
                    .setParameter("token", token)
                    .setParameter("newsessiontimeout", newsessiontimeout)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // COMENTÁRIO : apagar token de sessão

    public void deleteSessionToken(String token) {
        em.createNamedQuery("SessionToken.deleteSessionToken").setParameter("token", token).executeUpdate();
    }


    // COMENTÁRIO : apagar todos os tokens de sessão de um utilizador

    public void deleteAllSessionTokens(UserEntity user) {
        em.createNamedQuery("SessionToken.deleteAllSessionTokens").setParameter("user", user).executeUpdate();
    }


    // COMENTÁRIO : apagar todos os tokens de sessão inválidos (com session timeout caducado)
    public void deleteAllInvalidSessionTokens() {
      em.createNamedQuery("SessionToken.deleteAllInvalidSessionTokens").executeUpdate();
    }


// COMENTÁRIO : obter token por token

    public SessionTokenEntity findSessionTokenByToken(String token) {
        try {
            return em.createNamedQuery("SessionToken.findSessionTokenByToken", SessionTokenEntity.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

// passar voids pra booleans
}






