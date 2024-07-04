package dao;

import entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.List;
@Stateless
public class ProjectDao extends AbstractDao<ProjectEntity>{
    private static final long serialVersionUID = 1L;
    public ProjectDao() {
        super(ProjectEntity.class);
    }

    /**
     * o que é que esta classe tem de fazer
     * retornar as queries:
     *  retornar por id
     *  retornar lista dos membros associados a um projecto
     *  retornar lista de interesses associados a um projecto
     *  retornar lista de skills associados a um projecto
     *  retornar lista de tasks associados a um projecto
     *  retornar lista de materiais associados a um projecto
     *  retornar todos os projetos ordenados
     * lidar com nulls
     */
    public ProjectEntity getProjectByID(int id){
        try{
            return em.createNamedQuery("Project.getProjectByID", ProjectEntity.class).setParameter("id", id).getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<UserEntity> returnProjectMembers(int id){
        try{
            return em.createNamedQuery("Project.getMembers", UserEntity.class).setParameter("id", id).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<InterestEntity> returnProjectInterests(int id){
        try{
            return em.createNamedQuery("Project.getInterests", InterestEntity.class).setParameter("id", id).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<SkillEntity> returnProjectSkills(int id){
        try{
            return em.createNamedQuery("Project.getSkills", SkillEntity.class).setParameter("id", id).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<TaskEntity> returnProjectTasks(int id){
        try{
            return em.createNamedQuery("Project.getTasks", TaskEntity.class).setParameter("id", id).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<MaterialEntity> returnProjectMaterials(int id){
        try{
            return em.createNamedQuery("Project.getMaterials", MaterialEntity.class).setParameter("id", id).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<ProjectEntity> getAllProjectsOrdered(){
        try{
            return em.createNamedQuery("Project.getAll", ProjectEntity.class).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }



}
