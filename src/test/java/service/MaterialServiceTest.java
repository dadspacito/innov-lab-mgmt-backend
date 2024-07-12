package service;

import dao.MaterialDao;
import dto.MaterialDto;
import entity.MaterialEntity;
import enums.MaterialType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MaterialServiceTest {
    private MaterialService materialService;
    private MaterialDao materialDaoMock;

    @BeforeEach
    void setup() {
        //the class under test
        materialService = new MaterialService();
        //create mock objects
        materialDaoMock = mock(MaterialDao.class);
        //material service user the mock objects previously created
        materialService.setMaterialDao(materialDaoMock);

        //preparation
        MaterialEntity mEnt = new MaterialEntity();
        mEnt.setId(1);
        mEnt.setName("Arduino Uno");
        mEnt.setBrand("Arduino");
        mEnt.setType(MaterialType.COMPONENT);
        mEnt.setDescription("A microcontroller board for beginners");
        mEnt.setSerialNumber(1001);
        mEnt.setSupplier("Arduino Inc");
        mEnt.setSupplierContact(123456789);
        mEnt.setQuantity(5);
        mEnt.setObservations("Used for prototyping");

        Set<MaterialEntity> materials = new HashSet<MaterialEntity>();
        materials.add(mEnt);

        //define behaviour of materialDaoMock
        when(materialDaoMock.findMaterialByID(1)).thenReturn(mEnt);
        when(materialDaoMock.findMaterialByName("Arduino Uno")).thenReturn(mEnt);
        when(materialDaoMock.getAllMaterials()).thenReturn(materials);
        doNothing().when(materialDaoMock).persist(isA(MaterialEntity.class));
        doNothing().when(materialDaoMock).merge(isA(MaterialEntity.class));
        doNothing().when(materialDaoMock).remove(isA(MaterialEntity.class));
        doNothing().when(materialDaoMock).flush();
    }

    @Test
    void testGetMaterialByID(){
        int materialID = 1;
        MaterialEntity mEnt = new MaterialEntity();
        mEnt.setId(materialID);
        mEnt.setName("Arduino");
        MaterialEntity materialEntityResult = materialService.getMaterialsByID(materialID);
        //assert
        assertNotNull(materialEntityResult);
        assertEquals(materialEntityResult,mEnt);
        //verification
        verify(materialDaoMock,times(1)).findMaterialByID(materialID);
    }
    @Test
    void testGetMaterialByID_NullInterest(){
        int materialID = 2;
        assertNull(materialService.getMaterialsByID(2));
        verify(materialDaoMock,times(1)).findMaterialByID(materialID);
    }
    @Test
    void createNewMaterial(){
        MaterialDto mDto =  new MaterialDto();
        mDto.setName("motherboards");
        mDto.setBrand("motherboard planet");
        mDto.setType(MaterialType.COMPONENT);
        mDto.setDescription("A motherboard, its right there in the name");
        mDto.setSerialNumber(1001);
        mDto.setSupplier("Boards but not for surfing Inc");
        mDto.setSupplierContact(987654321);
        mDto.setQuantity(5);
        mDto.setObservations("Used for boarding (not ships)");
        materialService.addMaterialToDB(mDto);
        verify(materialDaoMock,times(1)).persist(isA(MaterialEntity.class));
    }

    /**
     * list material dto to entity
     * list entity to dto
     */
    @Test
    void testGetAllMaterials(){
        Set<MaterialEntity> materialList = new HashSet<MaterialEntity>();
        MaterialEntity m1 = new MaterialEntity();
        m1.setId(1);
        m1.setName("Arduino");
        MaterialEntity m2 = new MaterialEntity();
        m2.setId(2);
        m2.setName("Raspberry");
        materialList.add(m1);
        materialList.add(m2);

        when(materialDaoMock.getAllMaterials()).thenReturn(materialList);
        Set<MaterialDto> materialsDtoResult = materialService.getAllMaterials();
        //assert
        assertNotNull(materialsDtoResult);
        verify(materialDaoMock,times(1)).findAll();
    }

    @Test
    void testRemoveMaterialFromDB(){
        int materialID = 1;
        materialService.removeMaterialFromDB(materialID);
        //verification
        verify(materialDaoMock,times(2)).findMaterialByID(materialID);
        verify(materialDaoMock,times(1)).remove(isA(MaterialEntity.class));
    }
    @Test
    void testListProjectMaterialsDtoToEntity(){
        // Create MaterialDto objects
        Set<MaterialDto> materialsDtoSet = new HashSet<>();
        MaterialDto m1Dto = new MaterialDto();
        MaterialDto m2Dto = new MaterialDto();
        m1Dto.setId(1);
        m1Dto.setName("Material one");
        m2Dto.setId(2);
        m2Dto.setName("Material two");
        materialsDtoSet.add(m1Dto);
        materialsDtoSet.add(m2Dto);

        // Create MaterialEntity objects
        MaterialEntity m1Entity = new MaterialEntity();
        MaterialEntity m2Entity = new MaterialEntity();
        m1Entity.setId(1);
        m1Entity.setName("Material one");
        m2Entity.setId(2);
        m2Entity.setName("Material two");

        // Define behavior of the mock
        when(materialDaoMock.findMaterialByID(m1Dto.getId())).thenReturn(m1Entity);
        when(materialDaoMock.findMaterialByID(m2Dto.getId())).thenReturn(m2Entity);

        // Call the method to test
        Set<MaterialEntity> materialEntitiesSet = materialService.listProjectMaterialsDtoToEntity(materialsDtoSet);

        // Verify interactions with the mock
        verify(materialDaoMock, times(1)).findMaterialByID(m1Dto.getId());
        verify(materialDaoMock, times(1)).findMaterialByID(m2Dto.getId());

        // Assert the results
        assertNotNull(materialEntitiesSet);
        assertEquals(2, materialEntitiesSet.size());
    }
    @Test
    void testListProjectMaterialEntityToDto(){
        Set<MaterialEntity> materialEntitySet =  new HashSet<>();
        MaterialEntity m1Ent = new MaterialEntity();
        MaterialEntity m2Ent = new MaterialEntity();
        m1Ent.setId(1);
        m1Ent.setName("Material entity one");
        m2Ent.setId(2);
        m2Ent.setName("Material entity two");
        materialEntitySet.add(m1Ent);
        materialEntitySet.add(m2Ent);
        Set<MaterialDto> materialsDtoSet =  materialService.listProjectMaterialEntityToDto(materialEntitySet);
        assertNotNull(materialsDtoSet);
        assertEquals(2,materialsDtoSet.size());

    }





 }

