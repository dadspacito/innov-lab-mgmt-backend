package service;

import dao.InterestDao;
import dao.UserDao;
import dto.InterestDto;

import entity.InterestEntity;

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

    public List<InterestDto> getAllInterests() {
        List<InterestEntity> interestsEntities = interestDao.findAll();
        return interestsEntities.stream().map(this::mapInterestEntityToDto)
                            .collect(Collectors.toList());

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
        if (!isValidInterestName(interest.getName())){
            interestDao.persist(convertInterestDtoInEntity(interest));
            interestDao.flush();
        }
        else throw new EntityExistsException("That interest already exists " +  interest.getName());
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
    private boolean isValidInterestName(String name){
        return interestDao.findInterestByName(name) != null;
    }



    //função para converter novo interesse
    //tem de ter uma função de confirmar a identidade do user para ver se é permitido
    private InterestEntity convertInterestDtoInEntity(InterestDto i){
        InterestEntity iEnt = new InterestEntity();
        iEnt.setName(i.getName());
        iEnt.setCreatedAt(LocalDateTime.now());
        iEnt.setActive(true);
        return iEnt;
    }


    //associar um interest a um user;
    //funções set user e projetos a esta interests

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
