package service;

import dao.ProjectDao;
import dao.TaskDao;
import dto.BasicProjectDto;
import dto.DetailedProjectDto;
import dto.MaterialDto;
import dto.TaskDto;
import entity.*;
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
/**
 * Service class for managing projects and associated tasks.
 * <p>
 * This class provides methods for creating, retrieving, updating, and deleting projects and tasks.
 * It interacts with various DAOs (Data Access Objects) and services to handle business logic and data operations.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 */

@Stateless
public class ProjectService {
    /**
     * Injected service for managing project interests.
     */
    @Inject
    private InterestService interestService;
    /**
     * Injected service for managing users.
     */
    @Inject private UserService userService;
    /**
     * Injected service for managing tasks.
     */
    @Inject private TaskService taskService;
    /**
     * Injected service for managing skills.
     */
    @Inject private SkillService skillService;
    /**
     * Injected service for managing materials.
     */
    @Inject private MaterialService materialService;
    /**
     * EJB (Enterprise JavaBean) for accessing project data in the database.
     */
    @EJB
    private ProjectDao projectDao;
    /**
     * EJB (Enterprise JavaBean) for accessing task data in the database.
     */
    @EJB
    private TaskDao taskDao;
    /**
     * EJB (Enterprise JavaBean) for accessing workplace-related services.
     */
    @EJB WorkplaceService workplaceService;


    /**
     * Creates a new project and sets up an initial presentation task associated with it.
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Converts the provided {@link DetailedProjectDto} into a {@link ProjectEntity}.</li>
     *     <li>Persists the {@link ProjectEntity} into the database.</li>
     *     <li>Creates an initial presentation task for the project.</li>
     *     <li>Converts the created {@link TaskDto} into a {@link TaskEntity}.</li>
     *     <li>Associates the initial task with the project.</li>
     *     <li>Persists the {@link TaskEntity} into the database.</li>
     *     <li>Adds the task to the project's task collection and merges the project.</li>
     * </ol>
     * <p>
     * The method is annotated with {@code @Transactional}, meaning that all operations are performed within a single
     * transaction. If any step fails, the entire transaction will be rolled back.
     * </p>
     *
     * @param p The {@link DetailedProjectDto} object containing the details of the project to be created.
     *          This parameter should not be {@code null}.
     *
     * @throws IllegalArgumentException if the provided project details are invalid.
     *
     * @see ProjectEntity
     * @see DetailedProjectDto
     * @see TaskDto
     * @see TaskEntity
     * @see TaskService#createPresentationTask(DetailedProjectDto)
     */
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
    /**
     * Adds a new task to an existing project.
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Validates the project ID.</li>
     *     <li>Fetches the {@link ProjectEntity} associated with the given project ID.</li>
     *     <li>Converts the provided {@link TaskDto} into a {@link TaskEntity}.</li>
     *     <li>Persists the {@link TaskEntity} into the database.</li>
     *     <li>Adds the task to the project's task collection and merges the project.</li>
     * </ol>
     * </p>
     *
     * @param projectID The ID of the project to which the task should be added.
     *                  This parameter should be a valid project ID.
     * @param task The {@link TaskDto} object containing the details of the task to be added.
     *             This parameter should not be {@code null}.
     *
     * @throws IllegalArgumentException if the project ID is invalid or the task details are invalid.
     *
     * @see ProjectEntity
     * @see TaskDto
     * @see TaskEntity
     * @see TaskService#convertTaskDtoToEntity(TaskDto)
     */
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
    /**
     * Removes a task from a project.
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Validates the project ID.</li>
     *     <li>Fetches and removes the {@link TaskEntity} associated with the given task ID.</li>
     *     <li>If the task is part of the project, removes it from the project's task collection.</li>
     * </ol>
     * </p>
     *
     * @param taskID The ID of the task to be removed.
     *               This parameter should be a valid task ID.
     * @param projectID The ID of the project from which the task should be removed.
     *                  This parameter should be a valid project ID.
     *
     * @throws IllegalArgumentException if the project ID or task ID is invalid.
     *
     * @see ProjectEntity
     * @see TaskEntity
     * @see TaskDao#remove(TaskEntity)
     */
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
    /**
     * Retrieves all projects as a set of {@link DetailedProjectDto} objects.
     * <p>
     * This method fetches all projects from the database, orders them, and converts each
     * {@link ProjectEntity} to a {@link DetailedProjectDto}.
     * </p>
     *
     * @return A set of {@link DetailedProjectDto} objects representing all projects.
     *         Returns an empty set if no projects are found.
     *
     * @see ProjectEntity
     * @see DetailedProjectDto
     */
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
    /**
     * Converts a {@link DetailedProjectDto} to a {@link ProjectEntity}.
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Maps basic project details from the DTO to the entity.</li>
     *     <li>Sets the project's manager, workplace, interests, skills, materials, and members.</li>
     * </ol>
     * </p>
     *
     * @param p The {@link DetailedProjectDto} object containing the details of the project.
     *          This parameter should not be {@code null}.
     *
     * @return A {@link ProjectEntity} object populated with the details from the provided DTO.
     *
     * @see DetailedProjectDto
     * @see ProjectEntity
     */
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
    /**
     * Converts a {@link ProjectEntity} to a {@link DetailedProjectDto}.
     * <p>
     * This method creates a {@link DetailedProjectDto} object and populates its fields
     * with the corresponding values from the provided {@link ProjectEntity}.
     * </p>
     *
     * @param p The {@link ProjectEntity} object containing the details of the project.
     *          This parameter should not be {@code null}.
     *
     * @return A {@link DetailedProjectDto} object populated with the details from the provided entity.
     *
     * @see ProjectEntity
     * @see DetailedProjectDto
     * @see WorkplaceService#getWorkplaceDto(WorkplaceEntity)
     * @see UserService#convertUserEntityToProjectManager(UserEntity)
     * @see InterestService#listProjectEntityToDto(Set)
     * @see UserService#listUserEntityToMemberDto(Set)
     * @see MaterialService#listProjectMaterialEntityToDto(Set)
     * @see SkillService#listProjectSkillEntityToDto(Set)
     * @see TaskService#returnProjectTasksDto(Set)
     */
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
    /**
     * Checks if a project with the given ID exists.
     *
     * @param projectID The ID of the project to check.
     *                  This parameter should be a valid project ID.
     *
     * @return {@code true} if the project exists, {@code false} otherwise.
     *
     * @see ProjectDao#getProjectByID(int)
     */
    private boolean projectIsValid(int projectID){
        return projectDao.getProjectByID(projectID) != null;
    }
}
