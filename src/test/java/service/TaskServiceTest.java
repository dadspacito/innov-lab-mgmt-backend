package service;

import dao.ProjectDao;
import dao.TaskDao;
import dao.UserDao;
import dto.ProjectManagerDto;
import dto.ProjectMemberDto;
import dto.TaskDto;
import dto.UserDto;
import entity.ProjectEntity;
import entity.TaskEntity;
import entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskService taskService;

    private TaskDao taskDaoMock;

    private UserDao userDaoMock;

    private ProjectDao projectDaoMock;

    private UserService userServiceMock;

    private ProjectService projectServiceMock;

    @BeforeEach
    void setup(){
        taskService = new TaskService();
        taskDaoMock = mock(TaskDao.class);
        userDaoMock = mock(UserDao.class);
        projectDaoMock = mock(ProjectDao.class);
        userServiceMock = mock(UserService.class);
        projectServiceMock = mock(ProjectService.class);

        taskService.setTaskDao(taskDaoMock);
        taskService.setUserDao(userDaoMock);
        taskService.setProjectDao(projectDaoMock);
        taskService.setUserService(userServiceMock);
        taskService.setProjectService(projectServiceMock);

        TaskEntity tEnt = new TaskEntity();
        tEnt.setId(1);
        tEnt.setName("Make pasta");
        tEnt.setDescription("Esticar a pasta");
        UserEntity uEntOwner = new UserEntity();
        uEntOwner.setId(1);
        tEnt.setOwner(uEntOwner);
        tEnt.setProject(new ProjectEntity());

        UserEntity uEnt = new UserEntity();
        uEnt.setId(1);

        ProjectManagerDto pmDto = new ProjectManagerDto();
        pmDto.setId(1);

        ProjectMemberDto projectMemberDto = new ProjectMemberDto();
        projectMemberDto.setId(1);

        ProjectEntity pEnt = new ProjectEntity();
        pEnt.setId(1);

        when(taskDaoMock.returnTaskByID(1)).thenReturn(tEnt);
        when(userDaoMock.findUserById(1)).thenReturn(uEnt);
        when(projectDaoMock.getProjectByID(1)).thenReturn(pEnt);
        when(userServiceMock.convertUserEntityToProjectManager(uEnt)).thenReturn(pmDto);
        when(userServiceMock.ConvertUserEntityToProjectMembers(uEnt)).thenReturn(projectMemberDto);
        when(projectServiceMock.getProjectEntityByID(1)).thenReturn(pEnt);
        doNothing().when(taskDaoMock).merge(isA(TaskEntity.class));
    }

    @Test
    void testGetTaskByID(){
        int taskId = 1;

        TaskDto tDtoResult = taskService.getTaskByID(taskId);

        //assert
        assertNotNull(tDtoResult);
        assertEquals(tDtoResult.getId(), 1);
        assertEquals(tDtoResult.getName(), "Make pasta");

        //verification
        verify(taskDaoMock,times(1)).returnTaskByID(taskId);

    }
    @Test
    void testGetTaskByID_NullTask(){
        int taskId = 2;

        TaskDto tDtoResult = taskService.getTaskByID(taskId);

        //assert
        assertNull(tDtoResult);

        //verification
        verify(taskDaoMock,times(1)).returnTaskByID(taskId);
    }
    @Test
    void testUpdateTask(){
        int taskId = 1;
        int projectId = 1;
        TaskDto tDto = new TaskDto();

        taskService.updateTask(projectId, taskId, tDto);

        //verification
        verify(taskDaoMock,times(1)).merge(isA(TaskEntity.class));
        verify(projectServiceMock,times(1)).getProjectEntityByID(1);
    }
}
