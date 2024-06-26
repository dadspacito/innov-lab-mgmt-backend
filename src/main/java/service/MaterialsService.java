package service;


import dao.MaterialsDao;
import dto.MaterialsDto;
import entity.MaterialsEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;

@Stateless public class MaterialsService {
    @Inject
    private MaterialsDao materialsDao;

    //a lista que retorna do backend tem de ser transformada em DTO
    //adicionar material
    //remover material
    //retornar lista de materiais

    //função que recebe array entity backend e transforma em array dtos para mostrar no frontend
    private ArrayList<MaterialsDto> convertMaterialsEntityToDTO(ArrayList<MaterialsEntity> materialsList){
        ArrayList<MaterialsDto> materialsDtoList =  new ArrayList<>();
        MaterialsDto materialDTO = new MaterialsDto();
        for (MaterialsEntity material: materialsList){
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
    private MaterialsEntity convertMaterialDTOtoEntity(MaterialsDto material){
        MaterialsEntity me =  new MaterialsEntity();
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
    public void addMaterialToDB(MaterialsDto material){
        materialsDao.persist(convertMaterialDTOtoEntity(material));
        materialsDao.flush();
    }
    //retorna todos os materiais em DTO
    public ArrayList<MaterialsDto> getAllMaterials (){
        return convertMaterialsEntityToDTO((ArrayList<MaterialsEntity>) materialsDao.findAll());
    }
    //adicionar novo material À DB

    //apagar material da DB

}
