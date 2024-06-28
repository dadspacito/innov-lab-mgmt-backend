package dao;

import entity.ProjectEntity;
import entity.TaskEntity;
import entity.UserEntity;
import enums.TaskState;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Stateless public class TaskDao extends AbstractDao<TaskEntity> {
    private static final long serialVersionUID = 1L;
    public TaskDao(){super(TaskEntity.class);}

    //tem de retornar a task consoante um id user
    //tem de retornar tasks consoante id de um projeto
    //retornar tasks por state
    //tem que apagar a task
    //tem de filtrar a task


    //função que retorna as tasks de um user
    //aplicação: sber quantas tarefas um user especifico tem associadas a si. Vem filtrada por data e state da task
    //vem em arraylist/list de modo a vir sempre uma ou mais que uma.
    public List<TaskEntity> getTaskFromUserID(UserEntity owner){
        try{
            return em.createNamedQuery("task.findByOwnerID")
                    .setParameter("owner", owner).getResultList();
        }
        catch (NoResultException e){
            //estes detalhes tem de ser logados?
            return null;
        }
    };
    //as tarefas de um projeto tem sempre de ser retornadas consoante ordem e precedencia no tempo.
    //visualizar todas as tarefas associadas a um projeto
    //esta query ja vem filtrada
    public List<TaskEntity> getTaskFromProjectID(ProjectEntity project){
        try{
            return em.createNamedQuery("task.findByProjectID").setParameter("project", project).getResultList();
        }
        catch (NoResultException e){
            System.out.println("returned null");
            return null;
        }
    }

    //return tasks by state porque pode-se querer todas as tasks de um state especifico
    //find tasks by project and owner
    public List<TaskEntity> getTaskFromProjectAndUser(ProjectEntity project, UserEntity owner){
        try{
            return em.createNamedQuery("task.findByProjectAndOwner").setParameter("project", project)
                    .setParameter("owner", owner).getResultList();
        }
        catch (NoResultException e){
            return null;
        }
    }
    public TaskEntity returnTaskByID(int id){
        try{
            return (TaskEntity) em.createNamedQuery("task.findByID").setParameter("id", id).getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }
    public List<TaskEntity> getTasksFromProjectAndState(ProjectEntity p, TaskState s){
        try{
            return  em.createNamedQuery("task.findByProjectAndState")
                    .setParameter("project", p)
                    .setParameter("state", s).getResultList();
        }
        catch(NoResultException e){
            return null;
        }
    }



}
