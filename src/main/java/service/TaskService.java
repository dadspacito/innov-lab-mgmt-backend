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
 * Service class for managing tasks within projects.
 * <p>
 * This class provides methods for retrieving, creating, and converting tasks between DTOs (Data Transfer Objects) and entities.
 * It interacts with DAOs (Data Access Objects) to perform CRUD operations on tasks and handles task-related business logic.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 */

@Stateless
public class TaskService {
    @EJB
    private TaskDao taskDao;
    @EJB private UserDao userDao;
    @EJB private ProjectDao projectDao;
    @EJB private UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(TaskService.class);


    /**
     * Retrieves a task DTO by its associated entity.
     *
     * @param t The {@link TaskEntity} object representing the task to retrieve.
     *          This parameter should not be {@code null}.
     * @return The {@link TaskDto} representing the retrieved task, or {@code null} if the task does not exist or the user is invalid.
     */
    public TaskDto getTaskByID(TaskEntity t){
        if (isValidUser(t.getId())){
            taskDao.returnTaskByID(t.getId());
            LOGGER.info("sucessfully retrieved task" + t.getOwner().getEmail(), LocalDateTime.now());
            return convertTaskEntityToDto(t);

        }
        LOGGER.error("Error fetching task with id" + t.getOwner().getEmail(), LocalDateTime.now());
        return null;
    }
    /**
     * Retrieves a set of task entities from a list of task DTOs.
     *
     * @param t The list of {@link TaskDto} objects representing tasks to convert.
     *          This parameter should not be {@code null}.
     * @return A set of {@link TaskEntity} objects converted from the provided task DTOs.
     */
    public Set<TaskEntity> getTasksFromProject(List<TaskDto> t){
        return t.stream().map(this ::convertTaskDtoToEntity).collect(Collectors.toSet());
    }
    /**
     * Retrieves a list of task DTOs associated with a specific user.
     *
     * @param u The {@link UserEntity} object representing the user whose tasks to retrieve.
     *          This parameter should not be {@code null}.
     * @return An {@link ArrayList} of {@link TaskDto} objects representing tasks owned by the specified user,
     *         or {@code null} if the user does not exist or has no tasks.
     */
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
    /**
     * Converts a task entity to a task DTO.
     *
     * @param taskEnt The {@link TaskEntity} object representing the task entity to convert.
     *                This parameter should not be {@code null}.
     * @return The {@link TaskDto} object representing the converted task DTO.
     */
    private TaskDto convertTaskEntityToDto(TaskEntity taskEnt){
        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskEnt.getId());
        taskDto.setName(taskEnt.getName());
        taskDto.setDescription(taskEnt.getDescription());
        taskDto.setState(taskEnt.getState());
        taskDto.setStartDate(taskEnt.getStartDate());
        taskDto.setEndDate(taskEnt.getEndDate());
        taskDto.setOwner(userService.ConvertUserEntityToProjectMembers(userDao.findUserById(taskEnt.getOwner().getId())));
        taskDto.setProjectID(taskEnt.getProject().getId());
        return taskDto;
    }

    /**
     * Converts a task DTO to a task entity.
     *
     * @param taskDto The {@link TaskDto} object representing the task DTO to convert.
     *                This parameter should not be {@code null}.
     * @return The {@link TaskEntity} object representing the converted task entity.
     */
    public TaskEntity convertTaskDtoToEntity(TaskDto taskDto){
        TaskEntity taskEnt =  new TaskEntity();

        taskEnt.setActive(true);
        taskEnt.setName(taskDto.getName());
        taskEnt.setDescription(taskDto.getDescription());
        taskEnt.setStartDate(taskDto.getStartDate());
        taskEnt.setEndDate(taskDto.getEndDate());
        taskEnt.setOwner(userDao.findUserById(taskDto.getOwner().getId()));
        taskEnt.setState(taskDto.getState());
        taskEnt.setProject(projectDao.getProjectByID(taskDto.getProjectID()));
        return taskEnt;
    };
    /**
     * Checks if a user with the specified owner ID exists.
     *
     * @param ownerID The ID of the user to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    private boolean isValidUser(int ownerID){
        return userDao.findUserById(ownerID) != null;
    }
    /**
     * Creates a presentation task for a detailed project DTO.
     *
     * @param p The {@link DetailedProjectDto} object representing the detailed project for which to create the presentation task.
     *          This parameter should not be {@code null}.
     * @return The {@link TaskDto} object representing the created presentation task.
     */
    public TaskDto createPresentationTask(DetailedProjectDto p){
        TaskDto presentationTask = new TaskDto();
        presentationTask.setProjectID(p.getId());
        presentationTask.setName("Project Presentation");
        presentationTask.setDescription("Presentation of the project to the company");
        presentationTask.setStartDate(p.getEndDate().minusDays(1));
        presentationTask.setEndDate(p.getEndDate());
        //aqui tem de ser um dto de project member
        presentationTask.setOwner(userService.convertManagerToMember(p.getProjectManager()));
        presentationTask.setState(TaskState.PLANNED);
        return presentationTask;
    };
    /**
     * Converts a set of task DTOs to a set of task entities.
     *
     * @param t The set of {@link TaskDto} objects representing task DTOs to convert.
     *          This parameter should not be {@code null}.
     * @return A set of {@link TaskEntity} objects converted from the provided task DTOs.
     */
    public Set<TaskEntity> returnProjectTasksEntity(Set<TaskDto> t){
        return t.stream().map(this::convertTaskDtoToEntity).collect(Collectors.toSet());
    }
    /**
     * Converts a set of task entities to a set of task DTOs.
     *
     * @param t The set of {@link TaskEntity} objects representing task entities to convert.
     *          This parameter should not be {@code null}.
     * @return A set of {@link TaskDto} objects converted from the provided task entities.
     */
    public Set<TaskDto> returnProjectTasksDto(Set<TaskEntity> t){
        return t.stream().map(this :: convertTaskEntityToDto).collect(Collectors.toSet());
    }
    /**
     * Retrieves a task entity by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The {@link TaskEntity} object representing the retrieved task, or {@code null} if the task does not exist.
     */
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
