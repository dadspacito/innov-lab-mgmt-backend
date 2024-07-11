package service;

import dao.InterestDao;
import dao.UserDao;
import dto.InterestDto;

import entity.InterestEntity;

import entity.SkillEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Service class for managing interests.
 */
@Stateless
public class InterestService {

    @EJB
    private InterestDao interestDao;
    @EJB
    private UserDao userDao;
    /**
     * Sets the InterestDao. This is primarily used for mocking purposes in tests.
     *
     * @param interestDao the InterestDao to set
     */
    public void setInterestDao(InterestDao interestDao){
        this.interestDao = interestDao;
    };
    /**
     * Retrieves all interests as a set of InterestDto.
     *
     * @return a set of InterestDto representing all interests
     */
    @Transactional
    public Set<InterestDto> getAllInterests() {
        List<InterestEntity> interestsEntities = interestDao.findAll();
        return interestsEntities.stream().map(this::mapInterestEntityToDto)
                            .collect(Collectors.toSet());

    }
    /**
     * Converts a set of InterestDto to a set of InterestEntity.
     *
     * @param i the set of InterestDto to convert
     * @return a set of InterestEntity
     */
    public Set<InterestEntity> listProjectInterestsDtoToEntity(Set<InterestDto> i ){
        return i.stream()
                .map(interestDto -> interestDao.findInterestByID(interestDto.getId()))
                .collect(Collectors.toSet());
    }
    /**
     * Converts a set of InterestEntity to a set of InterestDto.
     *
     * @param i the set of InterestEntity to convert
     * @return a set of InterestDto
     */
    public Set<InterestDto> listProjectEntityToDto(Set<InterestEntity> i){
        return i.stream().map(this :: mapInterestEntityToDto).collect(Collectors.toSet());
    }
    /**
     * Converts an InterestEntity to an InterestDto.
     *
     * @param interest the InterestEntity to convert
     * @return the corresponding InterestDto
     */
    public InterestDto returnInterestDto(InterestEntity interest){
        return mapInterestEntityToDto(interest);
    }
    /**
     * Maps an InterestEntity to an InterestDto.
     *
     * @param interestEntity the InterestEntity to map
     * @return the corresponding InterestDto
     */
    private InterestDto mapInterestEntityToDto(InterestEntity interestEntity) {
        return new InterestDto(interestEntity.getName(), interestEntity.getId());
    }

    /**
     * Creates a new interest from the provided InterestDto.
     *
     * @param interest the InterestDto to create
     */
    @Transactional
    public void createNewInterest(InterestDto interest){
        InterestEntity iEnt = convertInterestDtoInEntity(interest);
        iEnt.setCreatedAt(LocalDateTime.now());
        iEnt.setActive(true);
        interestDao.persist(iEnt);
        interestDao.flush();
    }
    /**
     * Inactivates an interest by its ID.
     *
     * @param id the ID of the interest to inactivate
     * @throws EntityNotFoundException if the interest with the given ID is not found
     */
    @Transactional
    public void inactivateInterest(int id){
        if (isValidInterestID(id)){
            InterestEntity i = interestDao.findInterestByID(id);
            i.setActive(false);
            interestDao.merge(i);
            interestDao.flush();
        }
        else throw new EntityNotFoundException("That interest was not found in the database");
    }
    /**
     * Checks if an interest ID is valid.
     *
     * @param id the ID to check
     * @return true if the ID is valid, false otherwise
     */
    private boolean isValidInterestID(int id){
        return interestDao.findInterestByID(id) != null;
    }
    /**
     * Converts an InterestDto to an InterestEntity.
     *
     * @param i the InterestDto to convert
     * @return the corresponding InterestEntity
     */
    public InterestEntity convertInterestDtoInEntity(InterestDto i){
        InterestEntity iEnt = new InterestEntity();
        iEnt.setName(i.getName());
        return iEnt;
    }
    /**
     * Checks the validity of an interest.
     *
     * @param interest the InterestEntity to check
     * @return true if the interest is valid, false otherwise
     */
    @Transactional
    public boolean checkInterestValidity(InterestEntity interest){
        Set<InterestEntity> interests = interestDao.interestList();
        for (InterestEntity s : interests){
            if (s.getName().toLowerCase().replace(" ","").trim().matches(interest.getName().toLowerCase().replace(" ","").trim())){
                return false;
            }
        }
        return true;
    }
    /**
     * Retrieves an InterestEntity by its ID.
     *
     * @param id the ID of the interest
     * @return the InterestEntity, or null if not found
     */
    @Transactional
    public InterestEntity getInterestByID(int id){
        try {
            if(isValidInterestID(id)){
                return interestDao.findInterestByID(id);
            }
            else return null;
        }
        catch(NoResultException e){
            System.err.println("interest does not exist");
            return null;
        }
    }
    /**
     * Retrieves a set of active interests as InterestDto.
     *
     * @return a set of active InterestDto, or null if none found
     */
    @Transactional
    public Set<InterestDto> activeInterestDtoList(){
        try {
            Set<InterestDto> i = listProjectEntityToDto(interestDao.activeInterestList());
            return i;
        }
        catch (NoResultException e){
            System.err.println("no list");
            return null;
        }
    }
}
