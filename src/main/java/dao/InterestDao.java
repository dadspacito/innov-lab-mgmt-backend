package dao;


import entity.InterestEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Stateless
public class InterestDao extends AbstractDao<InterestEntity> {

    private static final long serialVersionUID = 1L;

    public InterestDao() {
        super(InterestEntity.class);
    }

    public InterestEntity findInterestByName(String name) {
        try {
            return em.createNamedQuery("Interest.findInterestByName", InterestEntity.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public InterestEntity findInterestByID(int id){
        try{
            return em.createNamedQuery("Interest.findInterestByID", InterestEntity.class).setParameter("id", id).getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }
    public Set<InterestEntity> interestList(){
        try{
            List<InterestEntity> interestList = em.createNamedQuery("Interest.findAllInterests", InterestEntity.class).getResultList();
            return new HashSet<>(interestList);
        }
        catch (NoResultException e){
            return null;
        }
    }
    public Set<InterestEntity> activeInterestList(){
        try{
            List<InterestEntity> interestList = em.createNamedQuery("Interest.findAllActiveInterests", InterestEntity.class).getResultList();
            return new HashSet<>(interestList);
        }
        catch (NoResultException e){
            return null;
        }
    }

}




