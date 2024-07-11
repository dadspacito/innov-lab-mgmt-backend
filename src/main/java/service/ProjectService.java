package service;

import api.UserResource;
import dao.*;
import dto.*;
import entity.*;
import enums.ProjectState;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.sql.SQLException;
import java.util.HashSet;
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
 * @author Pedro Monteiro, Daniel Silva
 * @version 1.0
 */

@Stateless
public class ProjectService {
    /**
     * Injected service for managing project interests.
     */
    @EJB
    private InterestService interestService;
    /**
     * Injected service for managing users.
     */
    @EJB private UserService userService;
    /**
     * Injected service for managing tasks.
     */
    @EJB private TaskService taskService;
    /**
     * Injected service for managing skills.
     */
    @EJB private SkillService skillService;
    /**
     * Injected service for managing materials.
     */
    @EJB private MaterialService materialService;
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
     * EJB (Enterprise JavaBean) for accessing user data in the database
     */
    @EJB private UserDao userDao;
    /**
     * EJB (Enterprise JavaBean) for accessing workplace data in the database
     */
    @EJB private WorkplaceDao workplaceDao;
    /**
     * EJB (Enterprise JavaBean) for accessing material data in the database
     */
    @EJB private MaterialDao materialDao;


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
            projectEntity.setProjectState(ProjectState.IN_PROGRESS);
            projectDao.persist(projectEntity);
            projectDao.flush();

            WorkplaceEntity workplace = workplaceService.getWorkplaceByID(projectEntity.getProjectWorkplace().getId());
            workplace.getProjects().add(projectEntity);

            Set<MaterialEntity> listMaterialEntity = materialService.listProjectMaterialsDtoToEntity(p.getProjectMaterials());
            for (MaterialEntity material : listMaterialEntity ){
                material.setProject(projectEntity);
            }

            Set<SkillEntity> listSkillEntities = skillService.listProjectSkillsDtoToEntity(p.getProjectSkills());
            for (SkillEntity skill : listSkillEntities){
                skill.getProjects().add(projectEntity);
            }

            Set<InterestEntity> listInterestEntities = interestService.listProjectInterestsDtoToEntity(p.getProjectInterests());
            for (InterestEntity interest : listInterestEntities){
                interest.getProjects().add(projectEntity);
            }

            Set<UserEntity> listUserEntities = userService.listMembersDtoToEntity(p.getProjectMembers());
            for (UserEntity user : listUserEntities){
                user.getProjects().add(projectEntity);
            }

            TaskDto initialTaskDto = taskService.createPresentationTask(p);
            TaskEntity initialTaskEntity = taskService.convertTaskDtoToEntity(initialTaskDto);
            initialTaskEntity.setProject(projectEntity);
            taskDao.persist(initialTaskEntity);
            taskDao.flush();
            projectEntity.getTasks().add(initialTaskEntity);

            projectDao.merge(projectEntity);
            projectDao.flush();
        }
        catch (NullPointerException e) {
            System.err.println("Error setting the project: " + e);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
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
            taskDao.remove(taskDao.returnTaskByID(taskID));
            taskDao.flush();
        }
    }
    /**
     * Removes a member from a project if the project and member are valid.
     *
     * @param projectID the ID of the project
     * @param memberID  the ID of the member to remove
     */
    @Transactional
    public void removeProjectMembers(int projectID, int memberID){
        if (projectIsValid(projectID)){
            ProjectEntity p = projectDao.getProjectByID(projectID);
            if (p.getProjectMembers().contains(userService.getUserByID(memberID))){
                p.getProjectMembers().remove(userDao.findUserById(memberID));
                projectDao.merge(p);
            }
            System.err.println("user is not in project");
        }
    }
    /**
     * Adds a member to a project if the project is valid and the member is not already in the project.
     *
     * @param projectID the ID of the project
     * @param memberID  the ID of the member to add
     */
    @Transactional
    public void addProjectMember(int projectID, int memberID){
        if (projectIsValid(projectID)){
            ProjectEntity p = projectDao.getProjectByID(projectID);
            if (!p.getProjectMembers().contains(userService.getUserByID(memberID))) {
                p.getProjectMembers().add(userDao.findUserById(memberID));
                UserEntity u = userService.getUserByID(memberID);
                u.getProjects().add(p);
                projectDao.merge(p);
            }
            else {
                System.err.println("user already in project");
            }
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
    public DetailedProjectDto getDetailedProject(int projectID) {
       if (projectIsValid(projectID)){
           return convertProjectEntityTodetailedProjectDto(projectDao.getProjectByID(projectID));
       }
        System.err.println("project does not exist");
       return null;
    }
    /**
     * Retrieves a set of basic project DTOs from all projects ordered.
     *
     * @return a set of {@link BasicProjectDto} representing basic project information.
     *         Returns null if no projects were found.
     */
    public Set<BasicProjectDto> getBasicProjects() {
        try{
            Set<ProjectEntity> p = projectDao.getAllProjectsOrdered();
            return p.stream().map(this::convertProjectEntityToBasicProjectDto).collect(Collectors.toSet());
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
        UserEntity manager = userService.defineManager(p.getProjectManager());
        pEnt.setManager(manager);
        pEnt.getProjectMembers().add(manager);
        pEnt.setProjectWorkplace(workplaceService.getWorkplaceByID(p.getProjectWorkplace().getId()));

        Set<InterestEntity> interestEntity = interestService.listProjectInterestsDtoToEntity(p.getProjectInterests());
        for (InterestEntity interest : interestEntity){
            pEnt.getInterests().add(interest);
        }

        Set<SkillEntity> skillsEntity =skillService.listProjectSkillsDtoToEntity(p.getProjectSkills());
        for (SkillEntity skill : skillsEntity){
            pEnt.getSkills().add(skill);
        }

        Set<MaterialEntity> materialEntities = materialService.listProjectMaterialsDtoToEntity(p.getProjectMaterials());
        for (MaterialEntity material : materialEntities){
            pEnt.getMaterials().add(material);
        }
        pEnt.getProjectMembers().addAll(userService.listMembersDtoToEntity(p.getProjectMembers()));
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
        ProjectPlanDto plan = new ProjectPlanDto();
        plan.setTaskList(taskService.returnProjectTasksDto(p.getTasks()));
        detailedProjectDto.setProjectPlan(plan);
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
    /**
     * Checks if the specified project is full. A project is considered full if it has 4 or more members.
     *
     * @param projectID the ID of the project to check.
     * @return {@code true} if the project has 4 or more members, {@code false} otherwise.
     * @throws EntityNotFoundException if the project with the specified ID does not exist.
     */
    public boolean projectIsFull(int projectID){
        ProjectEntity p = projectDao.getProjectByID(projectID);
        return p.getProjectMembers().size() >= 4;
    }
    /**
     * invite users to projects
     * criar uma set list nos users
     * é preciso uma set list nos projectos
     *
     */
    /**
     * tem que receber a query
     * @param p
     * @return
     */
    public Set<BasicProjectDto> listProjectEntityToBasicProject(Set<ProjectEntity> p){
        //isto aqui tem de ser uma query do dao em que vai buscar os projetos que pertencem ao id dele
        return p.stream().map(this::convertProjectEntityToBasicProjectDto).collect(Collectors.toSet());
    }
    /**
     * Converts a ProjectEntity to a BasicProjectDto.
     *
     * @param p the ProjectEntity to convert.
     * @return the converted BasicProjectDto.
     */
    private BasicProjectDto convertProjectEntityToBasicProjectDto(ProjectEntity p){
        BasicProjectDto basicProject =  new BasicProjectDto();
        basicProject.setId(p.getId());
        basicProject.setName(p.getName());
        basicProject.setDescription(p.getDescription());
        basicProject.setProjectState(p.getProjectState());
        basicProject.setProjectSkills(skillService.listProjectSkillEntityToDto(p.getSkills()));
        basicProject.setProjectInterests(interestService.listProjectEntityToDto(p.getInterests()));
        return basicProject;
    }
    /**
     * Retrieves a ProjectEntity by its ID.
     *
     * @param projectID the ID of the project.
     * @return the ProjectEntity if found, or null if not found.
     */
    public ProjectEntity getProjectEntityByID(int projectID){
        ProjectEntity p = projectDao.getProjectByID(projectID);
        if (p != null){
            return p;
        }
        else return null;
    }
    /**
     * Updates an existing project with the given details.
     *
     * @param projectID the ID of the project to update.
     * @param project the DetailedProjectDto containing updated project details.
     * @throws IllegalArgumentException if the project with the specified ID is not found.
     */
    @Transactional
    public void updateProject (int projectID, DetailedProjectDto project) {
        ProjectEntity p = getProjectEntityByID(projectID);
        if (p != null) {
            if (project.getName() != null) {
                p.setName(project.getName());
            }
            if (project.getDescription() != null) {
                p.setDescription(project.getDescription());
            }
            if (project.getStartDate() != null) {
                p.setStartDate(project.getStartDate());
            }
            if (project.getProjectState() != null) {
                p.setProjectState(project.getProjectState());
            }

            if (project.getProjectManager() != null) {
                UserEntity manager = userDao.findUserById(project.getProjectManager().getId());
                if (manager != null) {
                    p.setManager(manager);
                    manager.getManagedProjects().clear();
                    manager.getManagedProjects().add(p);
                } else System.err.println("Manager does not exist");
            }

            if (project.getProjectMembers() != null) {
                p.getProjectMembers().clear();
                Set<UserEntity> members = userService.listMembersDtoToEntity(project.getProjectMembers());
                for (UserEntity u : members) {
                    if (u != null) {
                        u.getManagedProjects().clear();
                        u.getProjects().add(p);
                    }
                    p.getProjectMembers().add(u);
                }
            }

            if (project.getProjectInterests() != null) {
                p.getInterests().clear();
                Set<InterestEntity> listInterestEntities = interestService.listProjectInterestsDtoToEntity(project.getProjectInterests());
                for (InterestEntity interest : listInterestEntities) {
                    if (interest != null) {
                        interest.getProjects().clear();
                        interest.getProjects().add(p);
                    }
                    p.getInterests().add(interest);
                }
            }

            if (project.getProjectSkills() != null) {
                p.getSkills().clear();
                Set<SkillEntity> listSkillEntities = skillService.listProjectSkillsDtoToEntity(project.getProjectSkills());
                for (SkillEntity skill : listSkillEntities) {
                    if (skill != null) {
                        skill.getProjects().clear();
                        skill.getProjects().add(p);
                    }
                    p.getSkills().add(skill);
                }
            }

            if (project.getProjectMaterials() != null) {
                Set<MaterialEntity> listMaterialEntity = materialService.listProjectMaterialsDtoToEntity(project.getProjectMaterials());
                p.getMaterials().clear();
                for (MaterialEntity material : listMaterialEntity) {
                    if (material != null) {
                        material.setProject(p);
                    }
                    p.getMaterials().add(material);
                }
            }

            if (project.getProjectPlan() != null && project.getProjectPlan().getTaskList() != null) {
                Set<TaskEntity> tasks = taskService.returnProjectTasksEntity(project.getProjectPlan().getTaskList());
                for (TaskEntity task : tasks) {
                    if (task != null) {
                        task.setProject(p);
                    }
                }
                p.setTasks(tasks);
            }

            if (project.getProjectWorkplace() != null) {
                WorkplaceEntity workplace = workplaceService.getWorkplaceByID(project.getProjectWorkplace().getId());
                workplace.getProjects().add(p);
                p.setProjectWorkplace(workplace);
            }
            projectDao.merge(p);
        } else {
            throw new IllegalArgumentException("Project with ID " + projectID + " not found.");
        }
    }
}
