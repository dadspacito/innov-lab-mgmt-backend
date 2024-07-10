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

@Stateless
public class InterestService {

    @EJB
    private InterestDao interestDao;
    @EJB
    private UserDao userDao;

    /**
     * falta a parte de criar interesses;
     * @return
     */





    // métodos de obter entidades
    // obter lista de todos interesses/keywords do sistema

    public Set<InterestDto> getAllInterests() {
        List<InterestEntity> interestsEntities = interestDao.findAll();
        return interestsEntities.stream().map(this::mapInterestEntityToDto)
                            .collect(Collectors.toSet());

    }
    //aqui tem de receber interesses de um projetoDTO e trasforma-los em entities
    //é apenas reference ao projeto
    //aqui foi definido como set, tem de ser retornado como set
    //aqui recebe um set de id's e tranforma-os en interesses
    public Set<InterestEntity> listProjectInterestsDtoToEntity(Set<InterestDto> i ){
        return i.stream()
                .map(interestDto -> interestDao.findInterestByID(interestDto.getId()))
                .collect(Collectors.toSet());
    }
    //transforma entities em set dto
    public Set<InterestDto> listProjectEntityToDto(Set<InterestEntity> i){
        return i.stream().map(this :: mapInterestEntityToDto).collect(Collectors.toSet());
    }
    //traz o nome o id. É o que vai para o frontend
    public InterestDto returnInterestDto(InterestEntity interest){
        return mapInterestEntityToDto(interest);
    }


    // mapper entidade dto interesse

    private InterestDto mapInterestEntityToDto(InterestEntity interestEntity) {
        return new InterestDto(interestEntity.getName(), interestEntity.getId());
    }


    //esta função cria um interest só para a db, sem definir users e projetos
    @Transactional
    public void createNewInterest(InterestDto interest){
        InterestEntity iEnt = convertInterestDtoInEntity(interest);
        iEnt.setCreatedAt(LocalDateTime.now());
        iEnt.setActive(true);
        interestDao.persist(iEnt);
        interestDao.flush();
    }
    @Transactional
    public void removeInterest(int id){
        System.out.println(id);
        if (isValidInterestID(id)){
            System.out.println(interestDao.findInterestByID(id));
            interestDao.remove(interestDao.findInterestByID(id));
            interestDao.flush();
        }
        else throw new EntityNotFoundException("That interest was not found in the database");
        //faz sentido aqui o logger?
    }

    private boolean isValidInterestID(int id){
        return interestDao.findInterestByID(id) != null;
    }
    public InterestEntity convertInterestDtoInEntity(InterestDto i){
        InterestEntity iEnt = new InterestEntity();
        iEnt.setName(i.getName());
        return iEnt;
    }
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
    private InterestEntity getInterestByID(int id){
        try {
            return interestDao.findInterestByID(id);
        }
        catch(NoResultException e){
            System.err.println("interest does not exist");
            return null;
        }
    }
}
