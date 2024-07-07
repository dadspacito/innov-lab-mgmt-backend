package service;


import dao.MaterialDao;
import dto.MaterialDto;
import entity.MaterialEntity;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless public class MaterialService {
    @Inject
    private MaterialDao materialDao;

    //a lista que retorna do backend tem de ser transformada em DTO
    //adicionar material
    //remover material
    //retornar lista de materiais

    //função que recebe array entity backend e transforma em array dtos para mostrar no frontend
    private MaterialDto convertMaterialsEntityToDTO(MaterialEntity material){
            MaterialDto materialDTO = new MaterialDto();
            materialDTO.setId(material.getId());
            materialDTO.setName(material.getName());
            materialDTO.setDescription(material.getDescription());
            materialDTO.setBrand(material.getBrand());
            materialDTO.setType(material.getType());
            materialDTO.setSerialNumber(material.getSerialNumber());
            materialDTO.setSupplier(material.getSupplier());
            materialDTO.setSupplierContact(material.getSupplierContact());
            materialDTO.setQuantity(material.getQuantity());
            materialDTO.setObservations(material.getObservations());
            //aqui falta depois o get project
            //materialsDtoList.add(materialDTO);
            return materialDTO;
    }
    //transforma um material DTO em material entity
    private MaterialEntity convertMaterialDTOtoEntity(MaterialDto material){
        MaterialEntity me =  new MaterialEntity();
        me.setName(material.getName());
        me.setBrand(material.getBrand());
        me.setDescription(material.getDescription());
        me.setType(material.getType());
        me.setSerialNumber(material.getSerialNumber());
        me.setSupplier(material.getSupplier());
        me.setSupplierContact(material.getSupplierContact());
        me.setQuantity(material.getQuantity());
        me.setObservations(material.getObservations());
        return me;
    }

    //adiciona um material à base de dados
    //aqui tem de ser transacional
    @Transactional
    public void addMaterialToDB(MaterialDto material){
        if (!materialNameIsValid(material.getName())){
            materialDao.persist(convertMaterialDTOtoEntity(material));
            materialDao.flush();
        }
    }


    //retorna todos os materiais em DTO
    public Set<MaterialDto> getAllMaterials (){
        List<MaterialEntity> m = materialDao.findAll();
        return m.stream().map(this::convertMaterialsEntityToDTO).collect(Collectors.toSet());
    }
    //adicionar novo material À DB
    @Transactional
    public void removeMaterialFromDB(int id){
        System.out.println(id);
        if (materialIDIsValid(id)){
            System.out.println(materialDao.findMaterialByID(id));
            materialDao.remove(materialDao.findMaterialByID(id));
            materialDao.flush();
        }
    }
    private boolean materialNameIsValid(String name){
        return materialDao.findMaterialByName(name)!= null;
    }
    private boolean materialIDIsValid(int id){
        return materialDao.findMaterialByID(id)!= null;
    }

    //isto nao estara bem feito porque precisa de pre validações sobre projeto onde esta inserido e afins
    public Set<MaterialEntity> listProjectMaterialsDtoToEntity(Set<MaterialDto> m){
        return m.stream().map(materialDto -> materialDao.findMaterialByID(materialDto.getId())).collect(Collectors.toSet());
    }
    public Set<MaterialDto> listProjectMaterialEntityToDto(Set<MaterialEntity> m){
        return m.stream().map(this ::convertMaterialsEntityToDTO).collect(Collectors.toSet());
    }
    private MaterialEntity getMaterialsByID(int id){
        try{
            return materialDao.findMaterialByID(id);
        }
        catch (NoResultException e){
            System.err.println("that material does not exist" + id);
            return null;
        }
    }

    //apagar material da DB

}
