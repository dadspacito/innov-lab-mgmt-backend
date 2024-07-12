package service;


import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import dao.WorkplaceDao;

import dto.*;

import entity.InterestEntity;
import entity.ProjectEntity;
import entity.SkillEntity;
import entity.UserEntity;
import enums.UserState;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import util.EmailSender;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Service class handling operations related to users in the system.
 */
@Stateless
public class UserService {

    @EJB private UserDao userDao;
    @EJB private WorkplaceService workplaceService;
    @EJB private SystemVariableDao systemVariableDao;
    @EJB private WorkplaceDao workplaceDao;
    @EJB private SessionTokenDao sessionTokenDao;
    @EJB private ProjectService projectService;
    @EJB private InterestService interestService;
    @EJB private SkillService skillService;

    public void setUserDao(UserDao userDao) {this.userDao = userDao;}

    public void setSessionTokenDao(SessionTokenDao sessionTokenDao) {this.sessionTokenDao = sessionTokenDao;}

    public void setWorkplaceDao(WorkplaceDao workplaceDao) {this.workplaceDao = workplaceDao;}

    /**
     * Registers a new user in the system.
     *
     * @param userDto Data Transfer Object containing user information for registration.
     * @return UserState indicating the status of user creation (e.g., CREATED, ALREADY_EXISTS).
     */
    @Transactional
    public UserState registerUser(UserDto userDto) {
        if (userDao.findUserByEmail(userDto.getEmail()) != null) {
            return UserState.ALREADY_EXISTS;
        }
        UserEntity user = new UserEntity();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        user.setAvatar("https://commons.wikimedia.org/wiki/File:Default_avatar_profile.jpg");
        user.setWorkplace(workplaceDao.find(userDto.getWorkplaceId()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(false);
        user.setConfirmed(false);
        user.setEmailToken(generateNewToken());
        user.setEmailTokenExpires(LocalDateTime.now().plusHours(1));
        String verificationLink = "http://localhost:3000/users/activations/" + user.getEmailToken();
        EmailSender.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationLink);
        userDao.persist(user);
         return UserState.CREATED;
    }
    /**
     * Retrieves a user entity by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return UserEntity corresponding to the given ID, or null if not found.
     */
    @Transactional
    public UserEntity getUserByID(int id){
        if (userIsValid(userDao.findUserById(id))){
            return userDao.findUserById(id);
        }
        else return null;
    }
    /**
     * Confirms a user's registration by validating the email token.
     *
     * @param emailToken The token sent to the user's email for verification.
     * @return true if the user is successfully confirmed, false otherwise.
     */
    public boolean confirmUser(String emailToken) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
            System.out.println("entrei no activate user no user service");
            return false;
        }
        else {
            user.setConfirmed(true);
            user.setEmailToken(null);
            user.setEmailTokenExpires(null);
            userDao.merge(user);
            return true;
        }
    }
    /**
     * Retrieves a user entity based on the session token.
     *
     * @param token The session token associated with the user.
     * @return UserEntity corresponding to the session token, or null if not found.
     */
    public UserEntity getUserByToken(String token) {
        return sessionTokenDao.findUserBySessionToken(token);
    }
    /**
     * Retrieves a list of project members available for selection.
     *
     * @return List of SelectProjectMembersDto representing available project members.
     */
    public List<SelectProjectMembersDto> membersAvailableProjects(){
        List<UserEntity> userEntList = userDao.findAll();
        return userEntList.stream().map(this::convertUserEntToMemberAvailable).collect(Collectors.toList());
    }
    /**
     * Converts a ProjectManagerDto object to a ProjectMemberDto object.
     *
     * @param manager ProjectManagerDto object to convert.
     * @return ProjectMemberDto representing the converted project manager.
     */
    public ProjectMemberDto convertManagerToMember(ProjectManagerDto manager){
        if (userIsValid(userDao.findUserById(manager.getId()))){
            ProjectMemberDto member =  new ProjectMemberDto();
            member.setName(manager.getName());
            member.setId(manager.getId());
            return member;
        }
        return null;
    }
    /**
     * Retrieves a UserEntity object from a ProjectManagerDto object.
     *
     * @param manager ProjectManagerDto object from which to retrieve the UserEntity.
     * @return UserEntity corresponding to the project manager, or null if not found.
     */
    public UserEntity getUserEntityFromManager(ProjectManagerDto manager){
        if (userIsValid(userDao.findUserById(manager.getId()))){
            return userDao.findUserById(manager.getId());
        }
        return null;
    }
    /**
     * Initiates a request for resetting a user's password.
     *
     * @param email The email address of the user requesting password reset.
     * @return true if the password reset request was successfully initiated, false otherwise.
     */
    public boolean requestPasswordReset(String email) {
        UserEntity user = userDao.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        user.setEmailToken(generateNewToken());
        user.setEmailTokenExpires(LocalDateTime.now().plusHours(1));
        userDao.merge(user);
        String resetLink = "http://localhost:3000/reset-password/" + user.getEmailToken();
        EmailSender.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetLink);
        return true;
    }
    /**
     * Resets a user's password based on the email token.
     *
     * @param emailToken The token sent to the user's email for password reset.
     * @param newPassword The new password to set for the user.
     * @return true if the password was successfully reset, false otherwise.
     */
    public boolean resetPassword(String emailToken, String newPassword) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
            return false;
        }
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setEmailToken(null);
        user.setEmailTokenExpires(null);
        userDao.merge(user);
        return true;
    }
    /**
     * Retrieves the header information of a user.
     *
     * @param user The UserEntity for which to retrieve the header information.
     * @return HeaderDto containing the user's nickname and avatar.
     */
    public HeaderDto getHeader (UserEntity user) {
        HeaderDto headerDto = new HeaderDto();
        if (user.getNickname() != null) {
            headerDto.setNickname(user.getNickname());
        } else {
            headerDto.setNickname(user.getFirstName()   + " " + user.getLastName());
        }
        headerDto.setAvatar(user.getAvatar());
        return headerDto;
    }
    /**
     * Retrieves a list of project members from a specified workplace.
     *
     * @param workplaceID The ID of the workplace from which to retrieve project members.
     * @return List of ProjectMemberDto representing project members in the workplace.
     */
    public List<ProjectMemberDto> returnProjectMembers(int workplaceID) {
        if (workplaceDao.getWorkplaceByID(workplaceID) != null) {
            return userDao.getUserByWorkplace(workplaceID).stream()
                    .map(this::ConvertUserEntityToProjectMembers)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    /**
     * Converts a UserEntity object to a ProjectMemberDto object.
     *
     * @param u UserEntity object to convert.
     * @return ProjectMemberDto representing the converted user entity.
     */
    public ProjectMemberDto ConvertUserEntityToProjectMembers(UserEntity u) {
        if (userIsValid(u)) {
            return new ProjectMemberDto(u.getId(), u.getFirstName(), u.getLastName(), u.getNickname(), LocalDateTime.now());
        }
        return null;
    }
    /**
     * Converts a UserEntity object to a ProjectManagerDto object.
     *
     * @param u UserEntity object to convert.
     * @return ProjectManagerDto representing the converted user entity as a project manager.
     */
    public ProjectManagerDto convertUserEntityToProjectManager(UserEntity u){
        if (userIsValid(u)){
            ProjectManagerDto manager = new ProjectManagerDto();
            manager.setId(u.getId());
            manager.setName(u.getFirstName() + " " + u.getLastName());
            manager.setManagerWorkplace(workplaceService.getWorkplaceDto(u.getWorkplace()));
            return manager;
        }
        return null;
    }
    /**
     * Retrieves a UserEntity object corresponding to a ProjectManagerDto object.
     *
     * @param manager ProjectManagerDto object from which to retrieve the UserEntity.
     * @return UserEntity corresponding to the project manager, or null if not found.
     */
    public UserEntity defineManager(ProjectManagerDto manager){
        try{
            if (userDao.findUserById(manager.getId())!= null){
                return userDao.findUserById(manager.getId());
            }
            return null;
        }
        catch (NoResultException e){
            return null;
        }
    }
    /**
     * Retrieves a UserEntity object from a ProjectMemberDto object.
     *
     * @param member ProjectMemberDto object from which to retrieve the UserEntity.
     * @return UserEntity corresponding to the project member, or null if not found.
     */
    public UserEntity getUserEntityFromProjectMember(ProjectMemberDto member){
        return userDao.findUserById(member.getId());
    }
    /**
     * Transforms a set of member IDs into a set of UserEntity objects.
     *
     * @param membersIDs Set of member IDs to transform.
     * @return Set of UserEntity representing the transformed member IDs.
     */
    public Set<UserEntity> transformIdIntoUserEntity(Set<Integer> membersIDs){
        Set<UserEntity> members = new HashSet<>();
        for (int i : membersIDs){
            members.add(userDao.findUserById(i));
        }
        return members;
    }
    /**
     * Converts a set of ProjectMemberDto objects to a set of UserEntity objects.
     *
     * @param member Set of ProjectMemberDto objects to convert.
     * @return Set of UserEntity representing the converted ProjectMemberDto objects.
     */
    public Set<UserEntity> listMembersDtoToEntity(Set<ProjectMemberDto> member){
        return member.stream().map(userDto -> userDao.findUserById(userDto.getId())).collect(Collectors.toSet());
    }
    /**
     * Converts a set of UserEntity objects to a set of ProjectMemberDto objects.
     *
     * @param user Set of UserEntity objects to convert.
     * @return Set of ProjectMemberDto representing the converted UserEntity objects.
     */
    public Set<ProjectMemberDto> listUserEntityToMemberDto(Set<UserEntity> user){
        return user.stream().map(this :: ConvertUserEntityToProjectMembers).collect(Collectors.toSet());
    }
    /**
     * Generates a new random token for email verification or password reset.
     *
     * @return String containing the generated token.
     */
    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }
    /**
     * Checks if a UserEntity object is valid based on existence and database retrieval.
     *
     * @param u UserEntity to validate.
     * @return true if the user is valid, false otherwise.
     */
    @Transactional
    private boolean userIsValid (UserEntity u){
        if (u == null){
            return false;
        }
        return userDao.findUserById(u.getId()) != null;
    }
    /**
     * Converts a UserEntity object to a SelectProjectMembersDto object for available project members.
     *
     * @param user UserEntity object to convert.
     * @return SelectProjectMembersDto representing the converted UserEntity.
     */
    private SelectProjectMembersDto convertUserEntToMemberAvailable(UserEntity user){
        SelectProjectMembersDto member = new SelectProjectMembersDto();
        member.setId(user.getId());
        member.setName(user.getFirstName() + " " + user.getLastName());
        member.setNickname(user.getNickname());
        member.setEmail(user.getEmail());
        return member;
    }
    /**
     * Converts a UserEntity object to a UserProfileDto object.
     *
     * @param u UserEntity object to convert.
     * @return UserProfileDto representing the converted UserEntity, or null if not found.
     */
    private UserProfileDto convertUserEntityToProfileDto(UserEntity u){
        if (userDao.findUserById(u.getId()) != null){
            UserProfileDto userProfile = new UserProfileDto();
            userProfile.setId(u.getId());
            userProfile.setFirstName(u.getFirstName());
            userProfile.setLastName(u.getLastName());
            userProfile.setNickname(u.getNickname());
            userProfile.setEmail(u.getEmail());
            userProfile.setBio(u.getBio());
            userProfile.setAvatar(u.getAvatar());
            userProfile.setPublicProfile(u.isPublicProfile());
            userProfile.setWorkplace(workplaceService.getWorkplaceDto(u.getWorkplace()));
            userProfile.setSkills(skillService.listProjectSkillEntityToDto(u.getSkills()));
            userProfile.setInterests(interestService.listProjectEntityToDto(u.getInterests()));
            userProfile.setIsInProject(projectService.listProjectEntityToBasicProject(u.getProjects()));
            userProfile.setIsInProject(projectService.listProjectEntityToBasicProject(userDao.getUserProjects(u.getId())));
            return userProfile;
        }
        return null;
    }

    public UserProfileDto getUserProfileDto(int userID){
        if (userIsValid(userDao.findUserById(userID))){
            return convertUserEntityToProfileDto(userDao.findUserById(userID));
        }
        System.err.println("user does not exist");
        return null;
    }

    /**
     * Edits the profile of a user based on the provided UserProfileDto.
     *
     * @param userID The ID of the user whose profile is being edited.
     * @param userDto Data Transfer Object containing updated user profile information.
     */
    @Transactional
    public void editProfile(int userID,UserProfileDto userDto){
        UserEntity p = DtoToEntityProfileEdition(userID,userDto);
        userDao.merge(p);
        userDao.flush();
    }
    /**
     * Converts a UserProfileDto object to a UserEntity object.
     *
     * @param userDto Data Transfer Object containing user profile information.
     * Does not edit the user interests and skill, see ${function} for that.
     * @return UserEntity object with updated profile information.
     */
    private UserEntity convertProfileDtoInUserEnt(UserProfileDto userDto){
        UserEntity u = userDao.findUserById(userDto.getId());
        u.setFirstName(userDto.getFirstName());
        u.setLastName(userDto.getLastName());
        u.setNickname(userDto.getNickname());
        u.setAvatar(userDto.getAvatar());
        u.setPublicProfile(userDto.isPublicProfile());
        u.setBio(userDto.getBio());
        return u;
    }
    /**
     * Converts a UserProfileDto object to update an existing UserEntity object for profile edition.
     *
     * @param userID The ID of the user whose profile is being edited.
     * @param userDto Data Transfer Object containing updated user profile information.
     * @return UserEntity object with updated profile information.
     */
    private UserEntity DtoToEntityProfileEdition(int userID, UserProfileDto userDto){
        UserEntity u = userDao.findUserById(userID);
        if (userDto.getFirstName() != null) {
            u.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            u.setLastName(userDto.getLastName());
        }
        if (userDto.getNickname() != null) {
            u.setNickname(userDto.getNickname());
        }
        if (userDto.getAvatar() != null) {
            u.setAvatar(userDto.getAvatar());
        }
        if (userDto.getBio() != null) {
            u.setBio(userDto.getBio());
        }
        return u;
    }
    /**
     * Adds a skill to a user identified by their ID.
     *
     * @param userID The ID of the user to whom the skill is being added.
     * @param skillID The ID of the skill to add.
     */
    @Transactional
    public void addSkillToUser(int userID, int skillID){
        UserEntity u = userDao.findUserById(userID);
        if (userIsValid(u)){
            SkillEntity s = skillService.getSKillByID(skillID);
            if (s != null){
                u.getSkills().add(s);
                s.addUser(u);
                userDao.merge(u);
                userDao.flush();
            }
            else System.err.println("Skill does not exist");
        }
        else System.err.println("user does not exist");
    }
    /**
     * Adds an interest to a user identified by their ID.
     *
     * @param userID The ID of the user to whom the interest is being added.
     * @param interestID The ID of the interest to add.
     */
    @Transactional
    public void addInterestToUser(int userID, int interestID){
        UserEntity u = userDao.findUserById(userID);
        if (userIsValid(u)){
            InterestEntity i = interestService.getInterestByID(interestID);
            if (i != null){
                u.getInterests().add(i);
                i.addUser(u);
                userDao.merge(u);
                userDao.flush();
            }
            else System.err.println("Interest does not exist");
        }
        else System.err.println("user does not exist");
    }
    /**
     * Removes an interest from a user identified by their ID.
     *
     * @param userID The ID of the user from whom the interest is being removed.
     * @param interestID The ID of the interest to remove.
     */
    @Transactional
    public void removeInterestFromUser(int userID, int interestID){
        UserEntity u = userDao.findUserById(userID);
        if (userIsValid(u)){
            InterestEntity i = interestService.getInterestByID(interestID);
            if (i != null){
                u.getInterests().remove(i);
                i.removeUser(u);
                userDao.merge(u);
                userDao.flush();
            }
            else System.err.println("Interest does not exist");
        }
        else System.err.println("user does not exist");
    }
    /**
     * Removes a skill from a user identified by their ID.
     *
     * @param userID The ID of the user from whom the skill is being removed.
     * @param skillID The ID of the skill to remove.
     */
    @Transactional
    public void removeSkillFromUser(int userID, int skillID){
        UserEntity u = userDao.findUserById(userID);
        if (userIsValid(u)){
            SkillEntity s = skillService.getSKillByID(skillID);
            if (s != null){
                u.getSkills().remove(s);
                s.removeUser(u);
                userDao.merge(u);
                userDao.flush();
            }
            else System.err.println("skill does not exist");
        }
        else System.err.println("user does not exist");
    }
    /**
     * Allows a user identified by their ID to leave a project identified by its ID.
     *
     * @param userID The ID of the user leaving the project.
     * @param projectID The ID of the project from which the user is leaving.
     */
    @Transactional
    public void leaveProject(int userID, int projectID){
        UserEntity u = userDao.findUserById(userID);
        if(u != null){
            ProjectEntity p = projectService.getProjectEntityByID(projectID);
            if (p != null){
                p.getProjectMembers().remove(u);
                u.getProjects().remove(p);
                userDao.merge(u);
                userDao.flush();
            }
        }
    }
    /**
     * Allows a user identified by their ID to join a project identified by its ID.
     *
     * @param userID The ID of the user joining the project.
     * @param projectID The ID of the project to join.
     */
    @Transactional
    public void joinProject(int userID, int projectID){
        UserEntity u = userDao.findUserById(userID);
        if(u != null){
            ProjectEntity p = projectService.getProjectEntityByID(projectID);
            if (p != null){
                p.getProjectMembers().add(u);
                u.getProjects().add(p);
                userDao.merge(u);
                userDao.flush();
            }
        }
    }
    /**
     * Retrieves a set of basic project information associated with a user identified by their ID.
     *
     * @param userID The ID of the user for whom to retrieve associated projects.
     * @return Set of BasicProjectDto representing the user's associated projects.
     * @throws NullPointerException if the user with the specified ID does not exist.
     */
    @Transactional
    public Set<BasicProjectDto> getUserProjects(int userID){
        UserEntity u = userDao.findUserById(userID);
        if (u != null){
            Set<ProjectEntity> projects = userDao.getUserProjects(userID);
            Set<BasicProjectDto> projectsDto = projectService.listProjectEntityToBasicProject(projects);
            return projectsDto;
        }
        else throw new NullPointerException("user is null");
    }
}






