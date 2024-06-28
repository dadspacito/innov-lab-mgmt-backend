package service;


import dao.MaterialDao;
import dto.MaterialDto;
import entity.MaterialEntity;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;

@Stateless public class MaterialService {
    @Inject
    private MaterialDao materialDao;

    //a lista que retorna do backend tem de ser transformada em DTO
    //adicionar material
    //remover material
    //retornar lista de materiais

    //função que recebe array entity backend e transforma em array dtos para mostrar no frontend
    private ArrayList<MaterialDto> convertMaterialsEntityToDTO(ArrayList<MaterialEntity> materialsList){
        ArrayList<MaterialDto> materialsDtoList =  new ArrayList<>();
        MaterialDto materialDTO = new MaterialDto();
        for (MaterialEntity material: materialsList){
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
            materialsDtoList.add(materialDTO);
        }
        return materialsDtoList;
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
    public void addMaterialToDB(MaterialDto material){
        materialDao.persist(convertMaterialDTOtoEntity(material));
        materialDao.flush();
    }
    //retorna todos os materiais em DTO
    public ArrayList<MaterialDto> getAllMaterials (){
        return convertMaterialsEntityToDTO((ArrayList<MaterialEntity>) materialDao.findAll());
    }
    //adicionar novo material À DB

    //apagar material da DB

}
