package service;

import dto.SkillDto;
import entity.SkillEntity;
import dao.SkillDao;
import enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SkillService {

    @EJB
    private SkillDao skillDao;

    // COMENTÁRIO serviços crud para a entidade Skill
    // acrescentar pra apagar, ou editar, ou listar, ou pesquisar, ou criar, ou ativar/desativar

    // MAS MAIS IMPORTANTE, associar a entidade Skill a User, para que fique já associada ao user que cria
    // e que possa ser associada a outros users, e obter lista de users com a skill , para sugerir projetos ou stats.


    public void createSkill(@NotNull SkillDto skillDto) {

        SkillType skillType;
        try {
            skillType = SkillType.fromString(skillDto.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid skill type provided: " + skillDto.getType());
        }

        SkillEntity skillEntity = new SkillEntity(skillDto.getName(), skillType);
       // skillEntity.setIsActive(skillDto.getIsActive());

        // persistir dao
        skillDao.persist(skillEntity);
    }




    // métodos de obter entidades
    // obter lista de todas as skills do sistema



    public List<SkillDto> getAllSkills() {
        List<SkillEntity> skillEntities = skillDao.findAll();
        return skillEntities.stream().map(this::mapSkillEntityToDto)
                            .collect(Collectors.toList());

    }



    // mapper entidade dto skill

    public SkillDto mapSkillEntityToDto(SkillEntity skillEntity) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skillEntity.getId());
        skillDto.setName(skillEntity.getName());
        skillDto.setType(skillEntity.getType().toString());
        return skillDto;
    }

    // mapper skill dto entidade

    public SkillEntity mapSkillDtoToEntity(SkillDto skillDto) {
        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setId(skillDto.getId());
        skillEntity.setName(skillDto.getName());
        skillEntity.setType(SkillType.fromString(skillDto.getType()));
        return skillEntity;
    }



}
