package service;

import dao.ProjectDao;
import dao.TaskDao;
import dao.UserDao;
import dto.*;
import entity.*;
import enums.ProjectState;
import enums.TaskState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

public class ProjectServiceTest {
    private ProjectService projectService;
    private ProjectDao projectDaoMock;
    private TaskDao taskDaoMock;
    private UserDao userDaoMock;
    private UserService userServiceMock;
    private WorkplaceService workplaceServiceMock;
    private SkillService skillServiceMock;
    private TaskService taskServiceMock;
    private InterestService interestServiceMock;
    private MaterialService materialServiceMock;

    @BeforeEach
    void setup(){
        projectService = new ProjectService();
        projectDaoMock = mock(ProjectDao.class);
        taskDaoMock = mock(TaskDao.class);
        userDaoMock = mock(UserDao.class);
        userServiceMock = mock(UserService.class);
        interestServiceMock = mock(InterestService.class);
        workplaceServiceMock = mock(WorkplaceService.class);
        skillServiceMock = mock(SkillService.class);
        taskServiceMock = mock(TaskService.class);
        materialServiceMock = mock(MaterialService.class);

        projectService.setProjectDao(projectDaoMock);
        projectService.setTaskDao(taskDaoMock);
        projectService.setUserDao(userDaoMock);
        projectService.setUserService(userServiceMock);
        projectService.setMaterialService(materialServiceMock);
        projectService.setInterestService(interestServiceMock);
        projectService.setSkillService(skillServiceMock);
        projectService.setTaskService(taskServiceMock);
        projectService.setWorkplaceService(workplaceServiceMock);

        //preparation
        ProjectEntity pEnt = new ProjectEntity();
        pEnt.setId(1);
        pEnt.setName("Project test");
        pEnt.setDescription("This project is for test purposes");
        pEnt.setStartDate(LocalDateTime.now());
        pEnt.setEndDate(LocalDateTime.now().plusDays(2));
        pEnt.setProjectState(ProjectState.IN_PROGRESS);
        UserEntity manager = new UserEntity();
        manager.setId(1);
        manager.setFirstName("Gestor");
        pEnt.setManager(manager);
        pEnt.getProjectMembers().add(manager);
        WorkplaceEntity workplace =  new WorkplaceEntity();
        workplace.setId(1);
        workplace.setLocation("Alfarelos");
        pEnt.setProjectWorkplace(workplace);

        Set<InterestEntity> mockInterestEntList = new HashSet<>();
        InterestEntity iEnt1 = new InterestEntity();
        iEnt1.setId(1);
        iEnt1.setName("Space");
        InterestEntity iEnt2 = new InterestEntity();
        iEnt1.setId(2);
        iEnt1.setName("Ocean");
        InterestEntity iEnt3 = new InterestEntity();
        iEnt1.setId(3);
        iEnt1.setName("Forestry");
        mockInterestEntList.add(iEnt1);
        mockInterestEntList.add(iEnt2);
        mockInterestEntList.add(iEnt3);
        pEnt.getInterests().addAll(mockInterestEntList);

        Set<SkillEntity> mockSkillEntList = new HashSet<>();
        SkillEntity sEnt1 = new SkillEntity();
        sEnt1.setId(1);
        sEnt1.setName("Juggling");
        SkillEntity sEnt2 = new SkillEntity();
        sEnt2.setId(2);
        sEnt2.setName("Darts");
        SkillEntity sEnt3 = new SkillEntity();
        sEnt3.setId(3);
        sEnt3.setName("Dominoes");
        mockSkillEntList.add(sEnt1);
        mockSkillEntList.add(sEnt2);
        mockSkillEntList.add(sEnt3);
        pEnt.getSkills().addAll(mockSkillEntList);

        Set<UserEntity> mockMembersEntList =  new HashSet<>();
        UserEntity uEnt1 = new UserEntity();
        UserEntity uEnt2 = new UserEntity();
        UserEntity uEnt3 = new UserEntity();
        UserEntity uEnt4 = new UserEntity();
        UserEntity uEnt5 = new UserEntity();
        uEnt1.setId(1);
        uEnt1.setFirstName("Marcus");
        uEnt1.setLastName("Tonius");
        uEnt2.setId(2);
        uEnt2.setFirstName("Alexandre");
        uEnt2.setLastName("O médio");
        uEnt3.setId(1);
        uEnt3.setFirstName("César");
        uEnt3.setLastName("Salada");
        uEnt4.setId(4);
        uEnt4.setFirstName("Júlia");
        uEnt4.setLastName("Andrade");
        uEnt4.setId(5);
        uEnt4.setFirstName("André");
        uEnt4.setLastName("Júlio");
        mockMembersEntList.add(uEnt1);
        mockMembersEntList.add(uEnt2);
        mockMembersEntList.add(uEnt3);
        pEnt.getProjectMembers().addAll(mockMembersEntList);

        Set<MaterialEntity> mockMaterialsEntList = new HashSet<>();
        MaterialEntity mEnt1 = new MaterialEntity();
        MaterialEntity mEnt2 = new MaterialEntity();
        MaterialEntity mEnt3 = new MaterialEntity();
        mEnt1.setId(1);
        mEnt1.setName("Madeira");
        mEnt2.setId(2);
        mEnt2.setName("Metal");
        mEnt3.setId(3);
        mEnt3.setName("Sílica");
        mockMaterialsEntList.add(mEnt1);
        mockMaterialsEntList.add(mEnt2);
        mockMaterialsEntList.add(mEnt3);
        pEnt.getMaterials().addAll(mockMaterialsEntList);

        Set<TaskEntity> mockTaskList = new HashSet<>();
        TaskEntity tEnt1 = new TaskEntity();
        TaskEntity tEnt2 = new TaskEntity();
        TaskEntity tEnt3 = new TaskEntity();
        tEnt1.setId(1);
        tEnt1.setName("task 1");
        tEnt1.setState(TaskState.PLANNED);
        tEnt1.setOwner(uEnt1);

        tEnt2.setId(2);
        tEnt2.setName("task 2");
        tEnt2.setState(TaskState.PLANNED);
        tEnt2.setOwner(uEnt2);

        tEnt3.setId(3);
        tEnt3.setName("task 3");
        tEnt3.setState(TaskState.PLANNED);
        tEnt3.setOwner(uEnt3);
        mockTaskList.add(tEnt1);
        mockTaskList.add(tEnt2);
        mockTaskList.add(tEnt2);
        pEnt.getTasks().addAll(mockTaskList);

        // Set up Task
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1);
        taskDto.setProjectID(1);
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1);

        //define behaviour of projectDaoMock
        when(projectDaoMock.getProjectByID(1)).thenReturn(pEnt);
        when(projectDaoMock.returnProjectInterests(1)).thenReturn(mockInterestEntList);
        when(projectDaoMock.returnProjectSkills(1)).thenReturn(mockSkillEntList);
        when(projectDaoMock.returnProjectMaterials(1)).thenReturn(mockMaterialsEntList);
        when(projectDaoMock.returnProjectMembers(1)).thenReturn(mockMembersEntList);
        when(projectDaoMock.returnProjectTasks(1)).thenReturn(mockTaskList);
        doNothing().when(projectDaoMock).persist(isA(ProjectEntity.class));
        doNothing().when(projectDaoMock).merge(isA(ProjectEntity.class));
        doNothing().when(projectDaoMock).remove(isA(ProjectEntity.class));
        doNothing().when(projectDaoMock).flush();

        //define behaviour of taskServiceMock
        when(taskServiceMock.convertTaskDtoToEntity(any())).thenReturn(taskEntity);

        //define behaviour of taskDaoMock
        doNothing().when(taskDaoMock).remove(isA(TaskEntity.class));
        when(taskDaoMock.returnTaskByID(1)).thenReturn(taskEntity);

        //define behaviour of userServiceMock
        when(userServiceMock.getUserByID(1)).thenReturn(uEnt1);
        when(userServiceMock.getUserByID(4)).thenReturn(uEnt4);
        when(userServiceMock.getUserByID(5)).thenReturn(uEnt5);

        //define behaviour of userDaoMock
        when(userServiceMock.getUserByID(4)).thenReturn(uEnt4);
        when(userServiceMock.getUserByID(5)).thenReturn(uEnt5);

    }
    @Test
    void testProjectIsfull(){
        assertFalse(projectService.projectIsFull(1));
        verify(projectDaoMock,times(1)).getProjectByID(1);
    }
    @Test
    void testCreateNewProject(){
        // Arrange
        DetailedProjectDto projectDtoMock = new DetailedProjectDto();
        projectDtoMock.setId(1);
        projectDtoMock.setName("Project Test");


        // Set up Workplace
        WorkplaceDto workplaceDto = new WorkplaceDto();
        workplaceDto.setId(1);
        projectDtoMock.setProjectWorkplace(workplaceDto);

        WorkplaceEntity workplaceEntity = new WorkplaceEntity();
        workplaceEntity.setId(1);
        when(workplaceServiceMock.getWorkplaceByID(1)).thenReturn(workplaceEntity);

        // Set up Materials
        Set<MaterialDto> materialDtos = new HashSet<>();
        MaterialDto mDto1 = new MaterialDto();
        mDto1.setId(1);
        materialDtos.add(mDto1);
        projectDtoMock.setProjectMaterials(materialDtos);

        MaterialEntity materialEntity = new MaterialEntity();
        materialEntity.setId(1);
        Set<MaterialEntity> materialEntities = new HashSet<>();
        materialEntities.add(materialEntity);
        when(materialServiceMock.listProjectMaterialsDtoToEntity(materialDtos)).thenReturn(materialEntities);

        // Set up Skills
        Set<SkillDto> skillDtos = new HashSet<>();
        SkillDto sDto1 = new SkillDto();
        sDto1.setId(1);
        skillDtos.add(sDto1);
        projectDtoMock.setProjectSkills(skillDtos);

        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setId(1);
        Set<SkillEntity> skillEntities = new HashSet<>();
        skillEntities.add(skillEntity);
        when(skillServiceMock.listProjectSkillsDtoToEntity(skillDtos)).thenReturn(skillEntities);

        // Set up Interests
        Set<InterestDto> interestDtos = new HashSet<>();
        InterestDto iDto1 = new InterestDto();
        iDto1.setId(1);
        interestDtos.add(iDto1);
        projectDtoMock.setProjectInterests(interestDtos);

        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setId(1);
        Set<InterestEntity> interestEntities = new HashSet<>();
        interestEntities.add(interestEntity);
        when(interestServiceMock.listProjectInterestsDtoToEntity(interestDtos)).thenReturn(interestEntities);

        // Set up Users
        Set<ProjectMemberDto> memberDtos = new HashSet<>();
        ProjectMemberDto uDto1 = new ProjectMemberDto();
        uDto1.setId(1);
        memberDtos.add(uDto1);
        projectDtoMock.setProjectMembers(memberDtos);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        Set<UserEntity> userEntities = new HashSet<>();
        userEntities.add(userEntity);
        when(userServiceMock.listMembersDtoToEntity(memberDtos)).thenReturn(userEntities);

        // Set up Task
        TaskDto initialTaskDto = new TaskDto();
        initialTaskDto.setId(1);
        when(taskServiceMock.createPresentationTask(projectDtoMock)).thenReturn(initialTaskDto);

        TaskEntity initialTaskEntity = new TaskEntity();
        initialTaskEntity.setId(1);
        when(taskServiceMock.convertTaskDtoToEntity(initialTaskDto)).thenReturn(initialTaskEntity);

        // Act
        projectService.createNewProject(projectDtoMock);

        // void method so no asserts

        // Verification
        verify(projectDaoMock, times(1)).persist(any(ProjectEntity.class));
        verify(projectDaoMock, times(2)).flush();
        verify(projectDaoMock, times(1)).merge(any(ProjectEntity.class));

        verify(workplaceServiceMock, times(2)).getWorkplaceByID(1);
        verify(materialServiceMock, times(2)).listProjectMaterialsDtoToEntity(materialDtos);
        verify(skillServiceMock, times(2)).listProjectSkillsDtoToEntity(skillDtos);
        verify(interestServiceMock, times(2)).listProjectInterestsDtoToEntity(interestDtos);
        verify(userServiceMock, times(2)).listMembersDtoToEntity(memberDtos);
        verify(taskServiceMock, times(1)).createPresentationTask(projectDtoMock);
        verify(taskServiceMock, times(1)).convertTaskDtoToEntity(initialTaskDto);
    }

    @Test
    void testAddNewTaskProject(){
        int projectId = 1;
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1);

        projectService.addNewTaskProject(projectId, taskDto);

        //verification
        verify(projectDaoMock,times(2)).getProjectByID(projectId);
        verify(taskDaoMock,times(1)).persist(isA(TaskEntity.class));
        verify(taskDaoMock, times(1)).flush();
        verify(projectDaoMock,times(1)).merge(isA(ProjectEntity.class));
        verify(projectDaoMock, times(1)).flush();

    }

    @Test
    void testRemoveTaskFromProject(){
        int projectId = 1;
        int taskId = 1;

        projectService.removeTaskFromProject(taskId, projectId);

        //verification
        verify(projectDaoMock,times(1)).getProjectByID(projectId);
        verify(taskDaoMock,times(1)).returnTaskByID(taskId);
        verify(taskDaoMock,times(1)).remove(isA(TaskEntity.class));
        verify(taskDaoMock, times(1)).flush();

    }

    @Test
    void testAddProjectMember(){
        int projectId = 1;
        int memberId = 4;

        projectService.addProjectMember(projectId, memberId);

        //verification
        verify(projectDaoMock,times(2)).getProjectByID(projectId);
        verify(projectDaoMock,times(1)).merge(isA(ProjectEntity.class));
        verify(userServiceMock,times(2)).getUserByID(memberId);
        verify(userDaoMock,times(1)).findUserById(memberId);
    }

    @Test
    void testAddProjectMember_MemberAlreadyExisted(){
        int projectId = 1;
        int memberId = 1;

        projectService.addProjectMember(projectId, memberId);

        //verification
        //nothing is merged
        verify(projectDaoMock,times(2)).getProjectByID(projectId);
        verify(projectDaoMock,times(0)).merge(isA(ProjectEntity.class));
        verify(userServiceMock,times(1)).getUserByID(memberId);
        verify(userDaoMock,times(0)).findUserById(memberId);
    }

    @Test
    void testRemoveProjectMembers(){
        int projectId = 1;
        int memberId = 1;

        projectService.removeProjectMembers(projectId, memberId);

        //verification
        verify(projectDaoMock,times(2)).getProjectByID(projectId);
        verify(projectDaoMock,times(1)).merge(isA(ProjectEntity.class));

    }

    @Test
    void testRemoveProjectMembers_MemberDoesNotExistInProject(){
        int projectId = 1;
        int memberId = 5;

        projectService.removeProjectMembers(projectId, memberId);

        //verification
        //nothing is merged
        verify(projectDaoMock,times(2)).getProjectByID(projectId);
        verify(projectDaoMock,times(0)).merge(isA(ProjectEntity.class));

    }
}
