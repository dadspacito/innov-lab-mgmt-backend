package service;


import dao.MaterialDao;
import dto.MaterialDto;
import entity.MaterialEntity;
import jakarta.ejb.EJB;
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
/**
 * Service class for managing materials.
 */
@Stateless public class MaterialService {
    @EJB private MaterialDao materialDao;
    //for testing purposes
    public void setMaterialDao(MaterialDao materialDao){this.materialDao = materialDao;}
    /**
     * Converts a MaterialEntity to a MaterialDto.
     *
     * @param material the MaterialEntity to convert
     * @return the corresponding MaterialDto
     */
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
            return materialDTO;
    }
    /**
     * Converts a MaterialDto to a MaterialEntity.
     *
     * @param material the MaterialDto to convert
     * @return the corresponding MaterialEntity
     */
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
    /**
     * Adds a new material to the database.
     *
     * @param material the MaterialDto to add
     */
    @Transactional
    public void addMaterialToDB(MaterialDto material){
        if (!materialNameIsValid(material.getName())){
            materialDao.persist(convertMaterialDTOtoEntity(material));
            materialDao.flush();
        }
    }
    /**
     * Retrieves all materials as a set of MaterialDto.
     *
     * @return a set of MaterialDto representing all materials
     */
    public Set<MaterialDto> getAllMaterials (){
        List<MaterialEntity> m = materialDao.findAll();
        return m.stream().map(this::convertMaterialsEntityToDTO).collect(Collectors.toSet());
    }
    /**
     * Removes a material from the database by its ID.
     *
     * @param id the ID of the material to remove
     */
    @Transactional
    public void removeMaterialFromDB(int id){
        if (materialIDIsValid(id)){
            materialDao.remove(materialDao.findMaterialByID(id));
            materialDao.flush();
        }
    }
    /**
     * Checks if a material name is valid (i.e., does not already exist in the database).
     *
     * @param name the name to check
     * @return true if the name is valid, false otherwise
     */
    private boolean materialNameIsValid(String name){
        return materialDao.findMaterialByName(name)!= null;
    }
    /**
     * Checks if a material ID is valid (i.e., exists in the database).
     *
     * @param id the ID to check
     * @return true if the ID is valid, false otherwise
     */
    private boolean materialIDIsValid(int id){
        return materialDao.findMaterialByID(id)!= null;
    }
    /**
     * Converts a set of MaterialDto to a set of MaterialEntity.
     *
     * @param m the set of MaterialDto to convert
     * @return a set of MaterialEntity
     */
    public Set<MaterialEntity> listProjectMaterialsDtoToEntity(Set<MaterialDto> m){
        return m.stream().map(materialDto -> materialDao.findMaterialByID(materialDto.getId())).collect(Collectors.toSet());
    }
    /**
     * Converts a set of MaterialEntity to a set of MaterialDto.
     *
     * @param m the set of MaterialEntity to convert
     * @return a set of MaterialDto
     */
    public Set<MaterialDto> listProjectMaterialEntityToDto(Set<MaterialEntity> m){
        return m.stream().map(this ::convertMaterialsEntityToDTO).collect(Collectors.toSet());
    }
    /**
     * Retrieves a MaterialEntity by its ID.
     *
     * @param id the ID of the material
     * @return the MaterialEntity, or null if not found
     */
    @Transactional
    public MaterialEntity getMaterialsByID(int id){
        try{
            return materialDao.findMaterialByID(id);
        }
        catch (NoResultException e){
            System.err.println("that material does not exist" + id);
            return null;
        }
    }
}
