package dao;
//este dão tem de retornar um material especifico,
//retornar materiais em projetos
//adicionar materiais
//remover materiais

import entity.MaterialEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless  public class MaterialDao extends AbstractDao<MaterialEntity> {
    private static final long serialVersionUID = 1L;

    public MaterialDao() {
        super(MaterialEntity.class);
    }
    //return material by id
    //return list of materials
    //find material by name?
    public MaterialEntity findMaterialByID(int id){
        try{
            return (MaterialEntity) em.createNamedQuery("Material.findMaterialByID")
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
    public MaterialEntity findMaterialByName(String name){
        try{
            return em.createNamedQuery("Material.findMaterialByName", MaterialEntity.class).setParameter("name",name).getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public Set<MaterialEntity> getAllMaterials (){
        try{
            List<MaterialEntity> materials = em.createNamedQuery("Material.getAllMaterials", MaterialEntity.class).getResultList();
            return new HashSet<>(materials);
        } catch(NoResultException e){
            return null;
        }
    }

}
