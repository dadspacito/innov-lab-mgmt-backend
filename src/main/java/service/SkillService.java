package service;

import api.HeaderResource;
import dto.SkillDto;
import entity.SkillEntity;
import dao.SkillDao;
import enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.classfilewriter.DuplicateMemberException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing skills, including CRUD operations and mappings between DTOs and entities.
 */
@Stateless
public class SkillService {
    private static final Logger LOGGER = LogManager.getLogger(HeaderResource.class);
    @EJB private SkillDao skillDao;
    /**
     * Creates a new skill in the database.
     *
     * @param skillDto the DTO containing skill information.
     */
    @Transactional
    //aqui lidar com duplicados
    public void createSkill(SkillDto skillDto) {
        SkillType skillType = mapStringToSkillType(skillDto.getType());
        SkillEntity skillEntity = new SkillEntity(skillDto.getName(), skillType);
        skillEntity.setIsActive(true);
        skillDao.persist(skillEntity);
    }
    /**
     * Checks if a skill entity is valid (not duplicate).
     *
     * @param skill the skill entity to be validated.
     * @return true if the skill is valid, false if it already exists.
     */
    @Transactional
    public boolean checkSkillValidity(SkillEntity skill){
        Set<SkillEntity> skills = skillDao.getAllSkillsOrdered();
        for (SkillEntity s : skills){
            if (s.getName().toLowerCase().replace(" ","").trim().matches(skill.getName().toLowerCase().replace(" ","").trim())){
                return false;
            }
        }
        return true;
    }
    /**
     * Converts a set of skill DTOs to skill entities.
     *
     * @param skillDtos the set of skill DTOs to convert.
     * @return the set of corresponding skill entities.
     */
    public Set<SkillEntity> listProjectSkillsDtoToEntity(Set<SkillDto> skillDtos) {
        return skillDtos.stream()
                .map(skillDto -> skillDao.findSkillByID(skillDto.getId()))
                .collect(Collectors.toSet());
    }
    /**
     * Converts a set of skill entities to skill DTOs.
     *
     * @param skill the set of skill entities to convert.
     * @return the set of corresponding skill DTOs.
     */
    public Set<SkillDto> listProjectSkillEntityToDto(Set<SkillEntity> skill){
        return skill.stream().map(this :: mapSkillEntityToDto).collect(Collectors.toSet());
    }
    /**
     * Deactivates (sets isActive to false) a skill by its ID.
     *
     * @param id the ID of the skill to deactivate.
     */
    @Transactional
    public void inactivateSkill(int id){
        if (isValidSkill(id)){
            try{
                SkillEntity s = getSkillByID(id);
                if (s.getIsActive()) {
                    s.setIsActive(false);
                    skillDao.merge(skillDao.findSkillByID(id));
                    LOGGER.info("Skill was inactivated from the database at " + LocalDateTime.now());
                }
                else System.err.println("Skill is already inactive");
            }
            catch (NoResultException e){
                LOGGER.error("Error removing skill with id " + id + "from the database at " + LocalDateTime.now());
            }
            catch(NullPointerException e){
                LOGGER.error("Skill is null");
            }
        }
    }
    /**
     * Activates (sets isActive to true) a skill by its ID.
     *
     * @param id the ID of the skill to activate.
     */
    @Transactional
    public void activateSkill(int id){
        if(isValidSkill(id)){
            try {
                SkillEntity s = skillDao.findSkillByID(id);
                if (!s.getIsActive()) {
                    s.setIsActive(true);
                    skillDao.merge(skillDao.findSkillByID(id));
                    LOGGER.info("Skill was activated from the database at " + LocalDateTime.now());
                }
                else System.err.println("that skill is active");

            }
           catch (NoResultException e){
                    LOGGER.error("Error removing skill with id " + id + "from the database at " + LocalDateTime.now());
                }
            catch(NullPointerException e){
                    LOGGER.error("Skill is null");
                }
            }
    }
    /**
     * Checks if a skill with the given ID exists in the database.
     *
     * @param id the ID of the skill to check.
     * @return true if the skill exists, false otherwise.
     */
    private boolean isValidSkill(int id){
        try {
            return skillDao.findSkillByID(id) != null;
        }
        catch (NoResultException e){
            System.err.println("Skill does not exist in the database");
            return false;
        }
    }
    /**
     * Retrieves a skill entity by its ID.
     *
     * @param id the ID of the skill to retrieve.
     * @return the skill entity if found, null otherwise.
     */
    private SkillEntity getSkillByID(int id){
        if(isValidSkill(id)) {
            return skillDao.findSkillByID(id);
        }
        else return null;
    }
    /**
     * Retrieves all skills from the database.
     *
     * @return a set of all skill DTOs.
     */
    public Set<SkillDto> getAllSkills() {
        Set<SkillEntity> skillEntities = skillDao.getAllSkillsOrdered();
        return skillEntities.stream().map(this::mapSkillEntityToDto)
                            .collect(Collectors.toSet());
    }
    /**
     * Maps a skill entity to a skill DTO.
     *
     * @param skillEntity the skill entity to map.
     * @return the corresponding skill DTO.
     */
    public SkillDto mapSkillEntityToDto(SkillEntity skillEntity) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skillEntity.getId());
        skillDto.setName(skillEntity.getName());
        skillDto.setType(skillEntity.getType().name());
        return skillDto;
    }
    /**
     * Maps a skill DTO to a skill entity.
     *
     * @param skillDto the skill DTO to map.
     * @return the corresponding skill entity.
     */
    public SkillEntity mapSkillDtoToEntity(SkillDto skillDto) {
        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setId(skillDto.getId());
        skillEntity.setName(skillDto.getName());
        skillEntity.setType(SkillType.valueOf(skillDto.getType()));
        return skillEntity;
    }
    /**
     * Converts a string representation of skill type to a SkillType enum value.
     *
     * @param type the string representation of skill type.
     * @return the corresponding SkillType enum value.
     * @throws IllegalArgumentException if an invalid skill type is provided.
     */
    private SkillType mapStringToSkillType(String type){
        try{
            return SkillType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid skill type provided: " +  type);
        }
    }
    /**
     * Retrieves a skill entity by its ID.
     *
     * @param id the ID of the skill to retrieve.
     * @return the skill entity if found, null otherwise.
     */
    @Transactional
    public SkillEntity getSKillByID(int id){
        try {
            if (isValidSkill(id)) {
                return skillDao.findSkillByID(id);
            }
            return null;
        }
        catch (NullPointerException e){
            System.err.println("skill is null");
            return null;
        }
    }
}
