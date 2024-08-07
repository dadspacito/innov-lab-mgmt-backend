package dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import entity.WorkplaceEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class WorkplaceDao extends AbstractDao<WorkplaceEntity> {

    private static final long serialVersionUID = 1L;

    public WorkplaceDao() {
        super(WorkplaceEntity.class);
    }

    // devolve um workplace a partir da localização
    public WorkplaceEntity findWorkplaceByLocation(String location) {
        try {
            return em.createNamedQuery("Workplace.findWorkplaceByLocation", WorkplaceEntity.class).setParameter("location", location)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    // devolve todos os workplaces
    public Set<WorkplaceEntity> findAllWorkplaces() {
        try{
            List<WorkplaceEntity> workplace =  em.createNamedQuery("Workplace.findAllWorkplaces", WorkplaceEntity.class).getResultList();

            return new HashSet<>(workplace);
        } catch (NoResultException e) {
            return null;
        }
    }
    public WorkplaceEntity getWorkplaceByID(int id){
        try{
            return em.createNamedQuery("Workplace.findWorkplaceByID", WorkplaceEntity.class).setParameter("id", id).getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }
}
