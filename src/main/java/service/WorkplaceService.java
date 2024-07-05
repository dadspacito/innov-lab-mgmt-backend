package service;

import dao.WorkplaceDao;
import entity.WorkplaceEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class WorkplaceService {
    @EJB
    private WorkplaceDao workplaceDao;

    public List<WorkplaceEntity> returnLocations(){
        return workplaceDao.findAll();
    }
    public WorkplaceEntity getWorkplaceByID(int id){
        if (workplaceDao.getWorkplaceByID(id)!= null){
            return workplaceDao.getWorkplaceByID(id);
        }
        else throw new EntityNotFoundException("that location does not exist");
    }
}
