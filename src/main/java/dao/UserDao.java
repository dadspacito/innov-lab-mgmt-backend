package dao;

import entity.ProjectEntity;
import entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class UserDao extends AbstractDao<UserEntity> {

    private static final long serialVersionUID = 1L;

    public UserDao() {
        super(UserEntity.class);
    }



    public UserEntity findUserByEmail(String email) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByEmail").setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }



    public UserEntity findUserById(int id) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
    public Set<ProjectEntity> getUserProjects(int userID){
        try {
            List<ProjectEntity> projectsSet =  em.createNamedQuery("User.findUserProjects", ProjectEntity.class).setParameter("userID", userID).getResultList();
            return new HashSet<>(projectsSet);
        } catch (NoResultException e){
            return Collections.emptySet();
        }
    }


    public UserEntity findUserByEmailToken(String emailToken) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByEmailToken").setParameter("emailToken", emailToken)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public UserEntity findUserByNickname(String nickname) {
        try {
            return (UserEntity) em.createNamedQuery("User.findUserByNickname").setParameter("nickname", nickname)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
    public List<UserEntity> getUserByWorkplace(int workplaceID){
        try{
            return em.createNamedQuery("User.findUserByWorkplace", UserEntity.class).setParameter("workplaceID", workplaceID).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }
}

