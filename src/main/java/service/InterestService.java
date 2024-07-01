package service;

import dao.InterestDao;
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

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class InterestService {

    @EJB
    private InterestDao interestDao;

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
    public List<InterestEntity> projectInterests(InterestDto i){
        //esta vai receber os project interests do frontend

        //converte-los e associa-los ao projeto
    }
    public InterestDto returnInterestDto(InterestEntity interest){
        return mapInterestEntityToDto(interest);
    }


    // mapper entidade dto interesse

    private InterestDto mapInterestEntityToDto(InterestEntity interestEntity) {
        return new InterestDto(interestEntity.getName(), interestEntity.getId());
    }
    @Transactional
    public void createNewInterest(InterestDto interest){
        if (!isValidInterest(interestDao.find(interest.getId()))){
            InterestEntity newInterest =  new InterestEntity(interest.getName());
            interestDao.persist(newInterest);
        }
        else throw new EntityExistsException("That interest already exists " +  interest.getName());
    }
    @Transactional
    public void removeInterest(InterestDto i){
        if (isValidInterest(interestDao.find(i.getId()))){
            interestDao.remove(interestDao.find(i.getId()));
        }
        else throw new EntityNotFoundException("That interest was not found in the database");
    }

    private boolean isValidInterest(InterestEntity i){
        return interestDao.find(i.getId()) != null;
    }


    // mapper interesse dto entidade

    //retornar interesses do projeto



}
