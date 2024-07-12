package service;

import dao.SessionTokenDao;
import dao.UserDao;
import dao.WorkplaceDao;
import dto.ProjectManagerDto;
import dto.ProjectMemberDto;
import dto.UserDto;
import dto.UserProfileDto;
import entity.UserEntity;
import entity.WorkplaceEntity;
import enums.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    UserService userService;
    UserDao userDaoMock;
    SessionTokenDao sessionTokenDaoMock;

    WorkplaceDao workplaceDaoMock;

    @BeforeEach
    void setup()
    {
        // the class under test
        userService = new UserService();

        // creating mock objects
        userDaoMock = mock(UserDao.class);
        sessionTokenDaoMock = mock(SessionTokenDao.class);
        workplaceDaoMock = mock(WorkplaceDao.class);

        // User service uses the mock objects previously created
        userService.setUserDao(userDaoMock);
        userService.setSessionTokenDao(sessionTokenDaoMock);
        userService.setWorkplaceDao(workplaceDaoMock);

        // preparation
        UserEntity uEnt = new UserEntity();
        uEnt.setId(1);
        uEnt.setFirstName("Fernando");
        uEnt.setLastName("Farinhas");
        uEnt.setNickname("Ferinha");
        uEnt.setEmail("ff@test.com");
        uEnt.setPassword("ff1234");
        uEnt.setAvatar("thisIsAString");
        uEnt.setBio("This is a bio");
        uEnt.setAdmin(false);
        uEnt.setPublicProfile(true);

        UserEntity uEnt2 = new UserEntity();
        uEnt2.setId(2);
        uEnt2.setFirstName("Maria");
        uEnt2.setLastName("Cruz");
        uEnt2.setNickname("Maricruz");
        uEnt2.setEmail("mc@test.com");
        uEnt2.setPassword("mc1234");
        uEnt2.setAvatar("thisIsAStringOfMc");
        uEnt2.setBio("This is a bio of Mc");
        uEnt2.setAdmin(true);
        uEnt2.setPublicProfile(false);

        UserEntity uEnt3 = new UserEntity();
        uEnt3.setId(3);
        uEnt3.setFirstName("Joao");
        uEnt3.setLastName("Jose");
        uEnt3.setNickname("JJ");
        uEnt3.setEmail("jj@test.com");
        uEnt3.setPassword("jj1234");
        uEnt3.setAvatar("thisIsAStringOfJj");
        uEnt3.setBio("This is a bio of Jj");
        uEnt3.setAdmin(false);
        uEnt3.setPublicProfile(true);

        Set<UserEntity> users = new HashSet<UserEntity>();
        users.add(uEnt);
        users.add(uEnt2);
        users.add(uEnt3);

        // define behaviour of userDaoMock
        when(userDaoMock.findUserById(1)).thenReturn(uEnt);
        when(userDaoMock.findUserById(2)).thenReturn(uEnt2);
        when(userDaoMock.findUserById(3)).thenReturn(uEnt3);
        when(userDaoMock.findUserById(4)).thenReturn(null);

        when(userDaoMock.findUserByEmail("ff@test.com")).thenReturn(uEnt);
        when(userDaoMock.findUserByEmail("mc@test.com")).thenReturn(uEnt2);
        when(userDaoMock.findUserByEmail("jj@test.com")).thenReturn(uEnt3);
        when(userDaoMock.findUserByEmail("")).thenReturn(null);

        //when(userDaoMock.findAllUsers()).thenReturn(uEnt);

        when(sessionTokenDaoMock.findUserBySessionToken("")).thenReturn(uEnt);
        when(sessionTokenDaoMock.findUserBySessionToken("")).thenReturn(uEnt2);
        when(sessionTokenDaoMock.findUserBySessionToken("")).thenReturn(uEnt3);
        when(sessionTokenDaoMock.findUserBySessionToken("nullToken")).thenReturn(null);

        when(userDaoMock.findUserByNickname("")).thenReturn(uEnt);
        when(userDaoMock.findUserByNickname("")).thenReturn(uEnt2);
        when(userDaoMock.findUserByNickname("")).thenReturn(uEnt3);
        when(userDaoMock.findUserByNickname("")).thenReturn(null);


        when(workplaceDaoMock.find(1)).thenReturn(new WorkplaceEntity());

        when(userDaoMock.findUserByEmail("activeUser")).thenReturn(null);
        when(userDaoMock.findUserByEmail("notFound")).thenReturn(null);
        when(userDaoMock.findUserByEmail("notFound")).thenReturn(null);

        doNothing().when(userDaoMock).persist(isA(UserEntity.class));
        doNothing().when(userDaoMock).merge(isA(UserEntity.class));

        when(sessionTokenDaoMock.findUserBySessionToken("sessionToken")).thenReturn(uEnt2);
        when(userDaoMock.findUserByEmailToken("emailToken")).thenReturn(uEnt);
        when(userDaoMock.findUserByEmailToken("nullEmailToken")).thenReturn(null);
    }

    @Test
    void testGetUserByID()
    {

        int userId = 1;

        UserEntity uEnt = new UserEntity();
        uEnt.setId(1);
        uEnt.setFirstName("Fernando");
        uEnt.setLastName("Farinhas");
        uEnt.setNickname("Ferinha");
        uEnt.setEmail("ff@test.com");
        uEnt.setPassword("ff1234");
        uEnt.setAvatar("thisIsAString");
        uEnt.setBio("This is a bio");
        uEnt.setAdmin(false);
        uEnt.setPublicProfile(true);

        UserEntity moqUEnt = userService.getUserByID(userId);

        //assert
        assertNotNull(moqUEnt);
        assertEquals(moqUEnt, uEnt);

        //verification
        verify(userDaoMock,times(3)).findUserById(userId);
    }


    @Test
    void testGetUserByID_UserNotFound()
    {

        int userId = 4;

        UserEntity uEnt = userService.getUserByID(userId);

        //assert
        assertNull(uEnt);

        //verification
        verify(userDaoMock,times(1)).findUserById(userId);
    }

    @Test
    void testConfirmUser_UserDoesNotExist()
    {

        String emailToken = "nullToken";

        boolean confirmation = userService.confirmUser(emailToken);

        //assert
        assertFalse(confirmation);

        //verification
        verify(userDaoMock,times(0)).merge(isA(UserEntity.class));
    }
    @Test
    void testGetUserByToken()
    {
        UserEntity user = userService.getUserByToken("sessionToken");

        UserEntity uEnt = new UserEntity();
        uEnt.setId(2);
        uEnt.setFirstName("Maria");
        uEnt.setLastName("Cruz");
        uEnt.setNickname("Maricruz");
        uEnt.setEmail("mc@test.com");
        uEnt.setPassword("mc1234");
        uEnt.setAvatar("thisIsAStringOfMc");
        uEnt.setBio("This is a bio of Mc");
        uEnt.setAdmin(true);
        uEnt.setPublicProfile(false);

        //assert
        assertEquals(user, uEnt);

        //verification
        verify(sessionTokenDaoMock,times(1)).findUserBySessionToken("sessionToken");
    }
    @Test
    void testEditProfile()
    {
        UserProfileDto uPEnt = new UserProfileDto();
        uPEnt.setId(1);

        userService.editProfile(1, uPEnt);

        //assert
        verify(userDaoMock,times(1)).merge(any());
    }

    @Test
    void testEditProfile_UserNotValid()
    {
        UserProfileDto uEnt = new UserProfileDto();
        uEnt.setId(3);

        userService.editProfile(3, uEnt);

        //assert
        verify(userDaoMock,times(0)).persist(any());
    }

    @Test
    void testGetUserEntityFromManager()
    {
        UserEntity uEnt = new UserEntity();
        uEnt.setId(1);
        uEnt.setFirstName("Fernando");
        uEnt.setLastName("Farinhas");
        uEnt.setNickname("Ferinha");
        uEnt.setEmail("ff@test.com");
        uEnt.setPassword("ff1234");
        uEnt.setAvatar("thisIsAString");
        uEnt.setBio("This is a bio");
        uEnt.setAdmin(false);
        uEnt.setPublicProfile(true);

        ProjectManagerDto pmDto = new ProjectManagerDto();
        pmDto.setId(1);

        UserEntity pmEnt = userService.getUserEntityFromManager(pmDto);

        //assert
        assertEquals(uEnt,pmEnt);
        verify(userDaoMock,times(3)).findUserById(1);
    }

    @Test
    void testGetUserEntityFromManager_UserNotValid()
    {
        ProjectManagerDto pmDto =  new ProjectManagerDto();
        pmDto.setId(7);

        UserEntity pmEnt = userService.getUserEntityFromManager(pmDto);

        //assert
        verify(userDaoMock,times(1)).findUserById(7);
        assertNull(pmEnt);
    }

    @Test
    void testConvertManagerToMember()
    {
        ProjectManagerDto pmDto =  new ProjectManagerDto();
        pmDto.setId(1);
        pmDto.setName("Fernando");

        ProjectMemberDto pmemDto = userService.convertManagerToMember(pmDto);

        //assert
        assertEquals(1, pmemDto.getId());
        assertEquals("Fernando", pmemDto.getName());
    }

    @Test
    void testConvertManagerToMember_ReturnsNull()
    {
        ProjectManagerDto pmDto =  new ProjectManagerDto();
        pmDto.setId(7);

        ProjectMemberDto pmemDto = userService.convertManagerToMember(pmDto);

        //assert
        assertNull(pmemDto);
    }

    @Test
    void testRequestPasswordReset()
    {
        boolean requestPassReset =  userService.requestPasswordReset("ff@test.com");

        //assert
        assertTrue(requestPassReset);
    }

    @Test
    void testRequestPasswordReset_EmailNotFound()
    {
        boolean requestPassReset =  userService.requestPasswordReset("");

        //assert
        assertFalse(requestPassReset);
    }

    @Test
    void testRequestPassword()
    {
        boolean requestPassReset =  userService.resetPassword("emailToken","22222");

        //assert
        assertTrue(requestPassReset);
    }

    @Test
    void testRequestPassword_EmailNotFound()
    {
        boolean requestPassReset =  userService.resetPassword("nullEmailToken","22222");

        //assert
        assertFalse(requestPassReset);
    }
}
