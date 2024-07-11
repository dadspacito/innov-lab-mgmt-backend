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
/**
 * Service class handling operations related to workplaces in the system.
 */
@Stateless
public class WorkplaceService {
    @EJB
    private WorkplaceDao workplaceDao;
    /**
     * Retrieves all workplaces and converts them to DTOs.
     *
     * @return Set of WorkplaceDto containing all workplaces in the system.
     */
    @Transactional
    public Set<WorkplaceDto> returnWorkplaces() {
        return workplaceDao.findAllWorkplaces().stream()
                .map(this::convertWorkplaceEntityToDto)
                .collect(Collectors.toSet());
    }
    /**
     * Retrieves a workplace entity by its ID and validates its existence.
     *
     * @param id The ID of the workplace to retrieve.
     * @return WorkplaceEntity corresponding to the given ID.
     * @throws EntityNotFoundException if the workplace with the specified ID does not exist.
     */
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
    /**
     * Retrieves the users associated with a specific workplace.
     *
     * @param workplace The WorkplaceEntity for which to retrieve associated users.
     * @return Set of UserEntity representing users associated with the workplace.
     */
    public Set<UserEntity> getUsersFromWorkplace(WorkplaceEntity workplace){
        System.out.println(workplace.getUsers());
        return workplace.getUsers();
    }
    /**
     * Converts a WorkplaceDto object to a WorkplaceEntity object.
     *
     * @param workplace The WorkplaceDto object to convert.
     * @return WorkplaceEntity corresponding to the provided WorkplaceDto.
     * @throws NullPointerException if the workplace with the specified ID does not exist.
     */
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
    /**
     * Converts a WorkplaceEntity object to a WorkplaceDto object.
     *
     * @param w The WorkplaceEntity object to convert.
     * @return WorkplaceDto representing the converted WorkplaceEntity.
     */
    public WorkplaceDto getWorkplaceDto(WorkplaceEntity w){
        return convertWorkplaceEntityToDto(w);
    }
    /**
     * Converts a WorkplaceEntity object to a WorkplaceDto object.
     *
     * @param workplace The WorkplaceEntity object to convert.
     * @return WorkplaceDto representing the converted WorkplaceEntity, or null if not valid.
     */
    private WorkplaceDto convertWorkplaceEntityToDto(WorkplaceEntity workplace){
        if (isValidWorkplace(workplace)){
            WorkplaceDto workplaceDto =  new WorkplaceDto();
            workplaceDto.setId(workplace.getId());
            workplaceDto.setWorkplace(workplace.getLocation());
            return workplaceDto;
        }
        return null;
    }
    /**
     * Checks if a WorkplaceEntity object is valid based on existence in the database.
     *
     * @param w The WorkplaceEntity to validate.
     * @return true if the workplace is valid, false otherwise.
     */
    private boolean isValidWorkplace(WorkplaceEntity w){
        return workplaceDao.getWorkplaceByID(w.getId()) != null;
    }
}
