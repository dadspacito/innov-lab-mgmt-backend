package dao;

import entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Set<UserEntity> returnProjectMembers(int id){
        try{
            List<UserEntity> userEntities =  em.createNamedQuery("Project.getMembers", UserEntity.class).setParameter("id", id).getResultList();
            return new HashSet<>(userEntities);
        }
        catch(NoResultException e){
            return null;
        }
    }
    public Set<InterestEntity> returnProjectInterests(int id){
        try{
            List<InterestEntity> interestEntities =  em.createNamedQuery("Project.getInterests", InterestEntity.class).setParameter("id", id).getResultList();
            return new HashSet<>(interestEntities);
        }
        catch(NoResultException e){
            return null;
        }
    }
    public Set<SkillEntity> returnProjectSkills(int id){
        try{
            List<SkillEntity> skillEntities = em.createNamedQuery("Project.getSkills", SkillEntity.class).setParameter("id", id).getResultList();
            return new HashSet<>(skillEntities);
        }
        catch(NoResultException e){
            return null;
        }
    }
    public Set<TaskEntity> returnProjectTasks(int id){
        try{
            List<TaskEntity> taskEntities =  em.createNamedQuery("Project.getTasks", TaskEntity.class).setParameter("id", id).getResultList();
            return new HashSet<>(taskEntities);
        }
        catch(NoResultException e){
            return null;
        }
    }
    public Set<MaterialEntity> returnProjectMaterials(int id){
        try{
            List<MaterialEntity> materialEntities =  em.createNamedQuery("Project.getMaterials", MaterialEntity.class).setParameter("id", id).getResultList();
            return new HashSet<>(materialEntities);
        }
        catch(NoResultException e){
            return null;
        }
    }
    public Set<ProjectEntity> getAllProjectsOrdered(){
        try{
            List<ProjectEntity> projects = em.createNamedQuery("Project.getAll", ProjectEntity.class).getResultList();
            return new HashSet<>(projects);
        }
        catch(NoResultException e){
            return Collections.emptySet();
        }
    }



}
