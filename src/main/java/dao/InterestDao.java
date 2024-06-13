/*package dao;


import entity.InterestEntity;
import entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;

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
    }*/




