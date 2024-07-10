package service;

import dao.WorkplaceDao;
import dto.WorkplaceDto;
import entity.UserEntity;
import entity.WorkplaceEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class WorkplaceService {
    @EJB
    private WorkplaceDao workplaceDao;

    @Transactional
    public Set<WorkplaceDto> returnWorkplaces() {
        return workplaceDao.findAllWorkplaces().stream()
                .map(this::convertWorkplaceEntityToDto)
                .collect(Collectors.toSet());
    }
    @Transactional
    public WorkplaceEntity getWorkplaceByID(int id) {
        System.out.println("getWorkplaceByID: Trying to find Workplace with ID: " + id);
        WorkplaceEntity workplace = workplaceDao.getWorkplaceByID(id);
        getUsersFromWorkplace(workplace);
        if (isValidWorkplace(workplace)) {
            System.out.println("getWorkplaceByID: Found valid Workplace: " + workplace);
            return workplace;
        } else {
            System.out.println("getWorkplaceByID: Workplace not valid or not found.");
            throw new NullPointerException("that location is null");
        }
    }

    public Set<UserEntity> getUsersFromWorkplace(WorkplaceEntity workplace){
        System.out.println(workplace.getUsers());
        return workplace.getUsers();
    }
    public WorkplaceEntity getWorkplaceEntityByDto(WorkplaceDto workplace){
        System.out.println("in workplace entity by dto");
        if (isValidWorkplace(workplaceDao.getWorkplaceByID(workplace.getId()))){
            System.out.println("inside valid getWorkplaceEntityByDto");
            System.out.println(workplace.getId());
            System.out.println(workplaceDao.getWorkplaceByID(workplace.getId()));
            WorkplaceEntity workplaceEntity = new WorkplaceEntity();
            workplaceEntity.setId(workplace.getId());
            workplaceEntity.setLocation(workplace.getWorkplace());
            return workplaceEntity;
        }
        System.err.println(workplace.getId());
        System.err.println(workplaceDao.getWorkplaceByID(workplace.getId()));
        throw new NullPointerException("that workplace does not exist");
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
