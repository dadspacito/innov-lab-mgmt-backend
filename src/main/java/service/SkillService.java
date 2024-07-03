package service;

import dto.SkillDto;
import entity.SkillEntity;
import dao.SkillDao;
import enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class SkillService {

    @EJB
    private SkillDao skillDao;

    // COMENTÁRIO serviços crud para a entidade Skill
    // acrescentar pra apagar, ou editar, ou listar, ou pesquisar, ou criar, ou ativar/desativar
    //faltam métodos de verificação privados
    // MAS MAIS IMPORTANTE, associar a entidade Skill a User, para que fique já associada ao user que cria
    // e que possa ser associada a outros users, e obter lista de users com a skill , para sugerir projetos ou stats.


    @Transactional
    public void createSkill(SkillDto skillDto) {
        SkillType skillType = mapStringToSkillType(skillDto.getType());
        SkillEntity skillEntity = new SkillEntity(skillDto.getName(), skillType);
        skillEntity.setIsActive(true);

        // persistir dao
        skillDao.persist(skillEntity);
    }
    //método que retorna lista de DTO's e transforma em Set
    public Set<SkillEntity> returnProjectSkills(List<SkillDto> s){
        return s.stream().map(this :: mapSkillDtoToEntity).collect(Collectors.toSet());
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
        skillDto.setType(skillEntity.getType().name());
        return skillDto;
    }

    // mapper skill dto entidade

    //método tem de ser privado

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
