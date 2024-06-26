package service;

import dao.InterestDao;
import dto.InterestDto;

import entity.InterestEntity;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class InterestService {

    @EJB
    private InterestDao interestDao;




    // métodos de obter entidades
    // obter lista de todos interesses/keywords do sistema

    public List<InterestDto> getAllInterests() {
        List<InterestEntity> interestsEntities = interestDao.findAll();
        return interestsEntities.stream().map(this::mapInterestEntityToDto)
                            .collect(Collectors.toList());

    }


    // mapper entidade dto interesse

    private InterestDto mapInterestEntityToDto(InterestEntity interestEntity) {
        return new InterestDto(interestEntity.getName(), interestEntity.getId());
    }


    // mapper interesse dto entidade



}
