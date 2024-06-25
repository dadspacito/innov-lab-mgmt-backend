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
        skillEntity.setIsActive(skillDto.getIsActive());

        // persistir dao
        skillDao.persist(skillEntity);
    }

    // obter lista de todas as skills do sistema

    public List<SkillEntity> getAllSkills() {
        return skillDao.findAll();
    }


}
