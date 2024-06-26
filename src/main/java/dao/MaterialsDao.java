package dao;
//este dão tem de retornar um material especifico,
//retornar materiais em projetos
//adicionar materiais
//remover materiais

import entity.MaterialsEntity;
import entity.SkillEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

@Stateless  public class MaterialsDao extends AbstractDao<MaterialsEntity> {
    private static final long serialVersionUID = 1L;

    public MaterialsDao() {
        super(MaterialsEntity.class);
    }
    //return material by id
    //return list of materials
    //find material by name?
    public MaterialsEntity findMaterialByID(int id){
        try{
            return (MaterialsEntity) em.createNamedQuery("Material.findMaterialByID")
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
}
