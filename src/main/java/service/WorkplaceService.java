package service;

import dao.WorkplaceDao;
import dto.WorkplaceDto;
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
    public WorkplaceDto getWorkplaceDto(WorkplaceEntity w){
        return convertWorkplaceEntityToDto(w);
    }
    private WorkplaceDto convertWorkplaceEntityToDto(WorkplaceEntity workplace){
        if (isValidWorkplace(workplace)){
            WorkplaceDto workplaceDto =  new WorkplaceDto();
            workplaceDto.setId(workplace.getId());
            workplaceDto.setWorkplace(workplace.getLocation());
            return workplaceDto;
        }
        return null;
    }
    private boolean isValidWorkplace(WorkplaceEntity w){
        return workplaceDao.getWorkplaceByID(w.getId()) != null;
    }
}
