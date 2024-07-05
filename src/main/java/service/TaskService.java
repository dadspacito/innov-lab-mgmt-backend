package service;

import api.SessionResource;
import dao.ProjectDao;
import dao.TaskDao;
import dao.UserDao;
import dto.DetailedProjectDto;
import dto.TaskDto;
import entity.TaskEntity;
import entity.UserEntity;
import enums.TaskState;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * o que é que o task service precisa?
 * retornar tarefas por user (retorna todas as tarefas de um user)
 * retornar tarefas por projeto (retorna todas as tarefas de um projeto)
 * retornar tarefas por projeto e user(retorna todas as tarefas de um user dentro de um projeto)
 * falta projeto
 *aqui cria a task inicial
 */

@Stateless
public class TaskService {
    @EJB
    private TaskDao taskDao;
    @EJB private UserDao userDao;
    @EJB private ProjectDao projectDao;
    private static final Logger LOGGER = LogManager.getLogger(TaskService.class);

    //create new task
    //aqui tem de verificar se user não é null e se projeto existe
    @Transactional
    public boolean createNewTask(TaskDto task){
        //falta adicionar a parte da verificação se o projeto não é null
        if (isValidUser(task.getOwnerID())){
            taskDao.persist(convertTaskDtoToEntity(task));
            taskDao.flush();
            LOGGER.info("Task was created " + userDao.findUserById(task.getOwnerID()).getEmail() + "at " +  LocalDateTime.now());
            return true;
        }
        else
        {
            LOGGER.error("task was no created " + LocalDateTime.now());
            return false;
        }
    }
    //delete task

    @Transactional
    public boolean deleteTask(TaskDto task){
        if (isValidUser(task.getOwnerID())){
            taskDao.remove(convertTaskDtoToEntity(task));
            taskDao.flush();
            LOGGER.info("task was sucessfully removed by " +userDao.findUserById(task.getOwnerID()).getEmail() + "at" + LocalDateTime.now() );
            return true;
        }
        LOGGER.error("There was an error deleting task " + userDao.findUserById(task.getOwnerID()).getEmail(),LocalDateTime.now()  );
        return false;
    }
    //@Transactional
    //public void addTaskToProject()

    /**
     * retornos:
     * task by id;
     * tasks by user;
     * tasks by project;
     * tasks by project and owner
     */
    //vai buscar uma task por id
    public TaskDto getTaskByID(TaskEntity t){
        if (isValidUser(t.getId())){
            taskDao.returnTaskByID(t.getId());
            LOGGER.info("sucessfully retrieved task" + t.getOwner().getEmail(), LocalDateTime.now());
            return convertTaskEntityToDto(t);

        }
        LOGGER.error("Error fetching task with id" + t.getOwner().getEmail(), LocalDateTime.now());
        return null;
    }
    public Set<TaskEntity> getTasksFromProject(List<TaskDto> t){
        return t.stream().map(this ::convertTaskDtoToEntity).collect(Collectors.toSet());
    }


    //é preciso ter acesso aos members do projeto para definir o id que entra
    public ArrayList<TaskDto> taskByOwner(UserEntity u){
        if(isValidUser(u.getId())){
            try {
                ArrayList<TaskDto> ownerTasksDto = new ArrayList<>();
                ArrayList<TaskEntity> ownerTasksEnt = (ArrayList<TaskEntity>) taskDao.getTaskFromUserID(u);
                for (TaskEntity t : ownerTasksEnt) {
                    ownerTasksDto.add(convertTaskEntityToDto(t));
                }
                LOGGER.info("Sucessfully retrieved tasks from " + u.getEmail(), LocalDateTime.now());
                return ownerTasksDto;
            }
            catch(NoResultException e){
                LOGGER.error("failed to return list of tasks from " + u.getEmail(), LocalDateTime.now());
                return null;
            }
        }
        return null;
    }
    //aqui faz se a verificação de que o projeto é válido
    //public ArrayList<TaskDto> getTasksByProjectAndState()
    /**
     * faltam tasks por owner e project
     *
     */


    /**
     * serviços privados de validação
     * verificação se user pertence a projeto
     * verify user
     * verify projects
     * conversão dao-dto e viceversa
     */
    private TaskDto convertTaskEntityToDto(TaskEntity taskEnt){
        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskEnt.getId());
        taskDto.setName(taskEnt.getName());
        taskDto.setDescription(taskEnt.getDescription());
        taskDto.setState(taskEnt.getState());
        taskDto.setStartDate(taskEnt.getStartDate());
        taskDto.setEndDate(taskEnt.getEndDate());
        taskDto.setOwnerID(taskEnt.getOwner().getId());
        //taskDto.setProjectID();
        return taskDto;
    }
    private TaskEntity convertTaskDtoToEntity(TaskDto taskDto){
        TaskEntity taskEnt =  new TaskEntity();
        taskEnt.setName(taskDto.getName());
        taskEnt.setDescription(taskDto.getDescription());
        taskEnt.setStartDate(taskDto.getStartDate());
        taskEnt.setEndDate(taskDto.getEndDate());
        UserEntity owner = userDao.findUserById(taskDto.getOwnerID());
        taskEnt.setOwner(owner);
        //ProjectEntity project = projectDao.findProjectByID(taskDto.getProjectByID());
        //taskEnt.setProject(taskDto.getProjectID());
        taskEnt.setState(taskDto.getState());
        return taskEnt;
    };
    //esta função é boa pratica construida desta maneira?
    //alterar esta função para ver se este user está associado ao projeto.
    //corre uma lista dos users no projeto e verificar se este id pertence a esta lista
    //devia ser is user project member
    private boolean isValidUser(int ownerID){
        return userDao.findUserById(ownerID) != null;

    }
    //função valid project


    //função que retorna a task inicial cujo start date é um dia antes do fim do projeto e end date na data de fim do projeto
    //implica que end date no projeto nao pode ser null
    //recebe um projeto

    //public TaskEntity createPresentationTask(){};
    public TaskDto createPresentationTask(DetailedProjectDto p){
        return new TaskDto(p.getEndDate().minusDays(1),p.getEndDate(),p.getProjectManager());
    };
    public Set<TaskEntity> returnProjectTasks(Set<Integer> t){
        return t.stream().map(this::getTaskByID).collect(Collectors.toSet());
    }
    public TaskEntity getTaskByID(int id){
        try{
            return taskDao.returnTaskByID(id);
        }
        catch (NoResultException e){
            System.err.println("that task does not exist");
            return null;
        }
    }






}
