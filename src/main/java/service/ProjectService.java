package service;

import dao.ProjectDao;
import dao.TaskDao;
import dto.BasicProjectDto;
import dto.DetailedProjectDto;
import dto.MaterialDto;
import dto.TaskDto;
import entity.MaterialEntity;
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
import java.util.Set;
import java.util.stream.Collectors;

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
        System.out.println(task);
        if (projectIsValid(projectID)){
            ProjectEntity pEnt = projectDao.getProjectByID(projectID);
            task.setProjectID(projectID);
            TaskEntity tEnt = taskService.convertTaskDtoToEntity(task);
            taskDao.persist(tEnt);
            taskDao.flush();
            pEnt.getTasks().add(tEnt);
            projectDao.merge(pEnt);
            projectDao.flush();
        }
    }
    @Transactional
    public void removeTaskFromProject(int taskID, int projectID){
        if (projectIsValid(projectID)){
            //tambem tem de remover a task do array dos projetos
            //ProjectEntity pEnt = projectDao.getProjectByID(projectID);
            //pEnt.getTasks().remove(taskService.getTaskByID(taskID));
            taskDao.remove(taskDao.returnTaskByID(taskID));
            taskDao.flush();

        }
    }
    public Set<DetailedProjectDto> getProjects(){
        try{
            List<ProjectEntity> p = projectDao.getAllProjectsOrdered();
            System.out.println(p);
           return p.stream().map(this::convertProjectEntityTodetailedProjectDto).collect(Collectors.toSet());
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
        pEnt.setProjectWorkplace(workplaceService.getWorkplaceByID(p.getProjectWorkplace().getId()));
        pEnt.getInterests().addAll(interestService.listProjectInterestsDtoToEntity(p.getProjectInterests()));
        pEnt.getSkills().addAll(skillService.listProjectSkillsDtoToEntity(p.getProjectSkills()));
        Set<MaterialEntity> materialEntities = materialService.listProjectMaterialsDtoToEntity(p.getProjectMaterials());
        for (MaterialEntity material : materialEntities){
            material.setProject(pEnt);
            pEnt.getMaterials().add(material);
        }
        pEnt.getProjectMembers().addAll(userService.listMembersDtoToEntity(p.getProjectMembers()));
        //pEnt.getTasks().addAll(taskService.returnProjectTasksEntity(p.getProjectTasks()));
        return pEnt;
    }
    private DetailedProjectDto convertProjectEntityTodetailedProjectDto(ProjectEntity p){
        DetailedProjectDto detailedProjectDto =  new DetailedProjectDto();
        detailedProjectDto.setId(p.getId());
        detailedProjectDto.setName(p.getName());
        detailedProjectDto.setDescription(p.getDescription());
        detailedProjectDto.setStartDate(p.getStartDate());
        detailedProjectDto.setEndDate(p.getEndDate());
        detailedProjectDto.setProjectWorkplace(workplaceService.getWorkplaceDto(p.getProjectWorkplace()));
        detailedProjectDto.setProjectState(p.getProjectState());
        detailedProjectDto.setProjectManager(userService.convertUserEntityToProjectManager(p.getManager()));
        detailedProjectDto.setProjectInterests(interestService.listProjectEntityToDto(p.getInterests()));
        detailedProjectDto.setProjectMembers(userService.listUserEntityToMemberDto(p.getProjectMembers()));
        detailedProjectDto.setProjectMaterials(materialService.listProjectMaterialEntityToDto(p.getMaterials()));
        detailedProjectDto.setProjectSkills(skillService.listProjectSkillEntityToDto(p.getSkills()));
        detailedProjectDto.setProjectTasks(taskService.returnProjectTasksDto(p.getTasks()));
        return detailedProjectDto;
    }
    //private BasicProjectDto basicProjectDto(ProjectEntity p){}
    //validação de existencia de projeto
    private boolean projectIsValid(int projectID){
        return projectDao.getProjectByID(projectID) != null;
    }
}
