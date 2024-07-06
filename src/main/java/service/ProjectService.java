package service;

import dao.ProjectDao;
import dao.TaskDao;
import dto.BasicProjectDto;
import dto.DetailedProjectDto;
import dto.TaskDto;
import entity.ProjectEntity;
import entity.TaskEntity;
import entity.WorkplaceEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class ProjectService {
    @Inject
    private InterestService interestService;
    @Inject private UserService userService;
    @Inject private TaskService taskService;
    @Inject private SkillService skillService;
    @Inject private MaterialService materialService;
    @EJB
    private ProjectDao projectDao;
    @EJB
    private TaskDao taskDao;
    @EJB WorkplaceService workplaceService;

    @Transactional
    public void createNewProject(DetailedProjectDto p){
        try{
            ProjectEntity projectEntity = convertProjectDtoToEntity(p);
            projectDao.persist(projectEntity);
            projectDao.flush();
            TaskDto initialTaskDto = taskService.createPresentationTask(p);
            TaskEntity initialTaskEntity = taskService.convertTaskDtoToEntity(initialTaskDto);
            initialTaskEntity.setProject(projectEntity);
            taskDao.persist(initialTaskEntity);
            taskDao.flush();
            projectEntity.getTasks().add(initialTaskEntity);
            projectDao.merge(projectEntity);
            projectDao.flush();
        }
        catch(IllegalArgumentException e){
            System.err.println("error setting the project");
        }
    }
    //adicionar uma nova task ao projeto
    @Transactional
    public void addNewTaskProject(int projectID, TaskDto task){
        if (projectIsValid(projectID)){
            //retornar o projeto pelo id
            ProjectEntity pEnt = projectDao.getProjectByID(projectID);
            //task entity
            TaskEntity tEnt = taskService.convertTaskDtoToEntity(task);
            //fazer persist da task
            taskDao.persist(tEnt);
            taskDao.flush();
            //adicionar a task ao projeto
            pEnt.getTasks().add(tEnt);
            //fazer merge ao projeto
            projectDao.merge(pEnt);
            projectDao.flush();
        }
    }
    @Transactional
    public void removeTaskFromProject(int taskID, int projectID){
        if (projectIsValid(projectID)){
            //tambem tem de remover a task do array dos projetos
            ProjectEntity pEnt = projectDao.getProjectByID(projectID);
            pEnt.getTasks().remove(taskService.getTaskByID(taskID));
            //taskDao.remove(taskDao.returnTaskByID(taskID));
            taskDao.flush();

        }
    }
    public List<ProjectEntity> getProjects(){
        try{
           return projectDao.getAllProjectsOrdered();
        }
        catch (NoResultException e){
            System.err.println("No projects were found");
            return null;
        }
    }
    private ProjectEntity convertProjectDtoToEntity (DetailedProjectDto p){
        ProjectEntity pEnt = new ProjectEntity();
        pEnt.setName(p.getName());
        pEnt.setDescription(p.getDescription());
        pEnt.setStartDate(p.getStartDate());
        pEnt.setEndDate(p.getEndDate());
        pEnt.setProjectState(p.getProjectState());
        pEnt.setManager((userService.defineManager(p.getProjectManager())));
        pEnt.setProjectWorkplace(workplaceService.getWorkplaceByID(p.getWorkplace()));
        pEnt.getInterests().addAll(interestService.projectInterests(p.getProjectInterests()));
        pEnt.getSkills().addAll(skillService.returnProjectSkills(p.getProjectSkills()));
        pEnt.getMaterials().addAll(materialService.returnProjectMaterials(p.getProjectMaterials()));
        pEnt.setProjectMembers(userService.returnMembersEntity(p.getProjectMembers()));
        return pEnt;
    }
    //validação de existencia de projeto
    private boolean projectIsValid(int projectID){
        return projectDao.getProjectByID(projectID) != null;
    }
}
