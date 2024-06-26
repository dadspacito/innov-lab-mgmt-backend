package service;

import dao.WorkplaceDao;
import entity.WorkplaceEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class WorkplaceService {
    @EJB
    private WorkplaceDao workplaceDao;

    public List<WorkplaceEntity> returnLocations(){
        return workplaceDao.findAll();
    }
}
