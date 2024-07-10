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

@Stateless
public class SkillService {
    /**
     * falta retornar apenas as skills ativas
     * reativar skills
     */
    private static final Logger LOGGER = LogManager.getLogger(HeaderResource.class);

    @EJB private SkillDao skillDao;
    @Transactional
    //aqui lidar com duplicados
    public void createSkill(SkillDto skillDto) {
        SkillType skillType = mapStringToSkillType(skillDto.getType());
        SkillEntity skillEntity = new SkillEntity(skillDto.getName(), skillType);
        skillEntity.setIsActive(true);
        skillDao.persist(skillEntity);
    }
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

    //***********************//
    //****LIST METHODS******//
    //**********************//
    public Set<SkillEntity> listProjectSkillsDtoToEntity(Set<SkillDto> skillDtos) {
        return skillDtos.stream()
                .map(skillDto -> skillDao.findSkillByID(skillDto.getId()))
                .collect(Collectors.toSet());
    }
    public Set<SkillDto> listProjectSkillEntityToDto(Set<SkillEntity> skill){
        return skill.stream().map(this :: mapSkillEntityToDto).collect(Collectors.toSet());
    }
    //delete skill
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

    //verifica se skill existe metodo privado
    private boolean isValidSkill(int id){
        try {
            return skillDao.findSkillByID(id) != null;
        }
        catch (NoResultException e){
            System.err.println("Skill does not exist in the database");
            return false;
        }
    }
    private SkillEntity getSkillByID(int id){
        if(isValidSkill(id)) {
            return skillDao.findSkillByID(id);
        }
        else return null;
    }
    public Set<SkillDto> getAllSkills() {
        Set<SkillEntity> skillEntities = skillDao.getAllSkillsOrdered();
        return skillEntities.stream().map(this::mapSkillEntityToDto)
                            .collect(Collectors.toSet());
    }
    public SkillDto mapSkillEntityToDto(SkillEntity skillEntity) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skillEntity.getId());
        skillDto.setName(skillEntity.getName());
        skillDto.setType(skillEntity.getType().name());
        return skillDto;
    }
    public SkillEntity mapSkillDtoToEntity(SkillDto skillDto) {
        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setId(skillDto.getId());
        skillEntity.setName(skillDto.getName());
        skillEntity.setType(SkillType.valueOf(skillDto.getType()));
        return skillEntity;
    }
    private SkillType mapStringToSkillType(String type){
        try{
            return SkillType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid skill type provided: " +  type);
        }
    }
}
