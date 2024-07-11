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

// função que vai adicionar user
    // recebe user dto
    // DTO vai ser válido quando chega aqui
    // incluir a validação das entidades associadas. Se workplace não existir, retornar NOT_FOUND

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
    @Transactional
    public UserEntity getUserByID(int id){
        if (userIsValid(userDao.findUserById(id))){
            return userDao.findUserById(id);
        }
        else return null;
    }

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
    public UserEntity getUserByToken(String token) {
        return sessionTokenDao.findUserBySessionToken(token);
    }
    public List<SelectProjectMembersDto> membersAvailableProjects(){
        List<UserEntity> userEntList = userDao.findAll();
        return userEntList.stream().map(this::convertUserEntToMemberAvailable).collect(Collectors.toList());
    }

    public UserState checkUserState (String email) {

        UserEntity user = userDao.findUserByEmail(email);
        /// retornar por cada tipo de condição
        if (user == null) {
            return UserState.NOT_FOUND;
        }
        if (!user.isConfirmed()) {
            return UserState.NOT_CONFIRMED;
        }
       /* if (user.isActive() == false) {
            return UserState.DELETED;
        }*/
        return UserState.ACTIVE;
    }

    public ProjectMemberDto convertManagerToMember(ProjectManagerDto manager){
        if (userIsValid(userDao.findUserById(manager.getId()))){
            ProjectMemberDto member =  new ProjectMemberDto();
            member.setName(manager.getName());
            member.setId(manager.getId());
            return member;
        }
        return null;
    }
    private UserEntity getUserEntityFromManager(ProjectManagerDto manager){
        if (userIsValid(userDao.findUserById(manager.getId()))){
            return userDao.findUserById(manager.getId());
        }
        return null;
    }
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


    public List<ProjectMemberDto> returnProjectMembers(int workplaceID) {
        if (workplaceDao.getWorkplaceByID(workplaceID) != null) {
            return userDao.getUserByWorkplace(workplaceID).stream()
                    .map(this::ConvertUserEntityToProjectMembers)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    public ProjectMemberDto ConvertUserEntityToProjectMembers(UserEntity u) {
        if (userIsValid(u)) {
            return new ProjectMemberDto(u.getId(), u.getFirstName(), u.getLastName(), u.getNickname(), LocalDateTime.now());
        }
        return null;
    }
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
    //metodo privado
    public UserEntity getUserEntityFromProjectMember(ProjectMemberDto member){
        return userDao.findUserById(member.getId());
    }
    //mudar nome depois
    public Set<UserEntity> transformIdIntoUserEntity(Set<Integer> membersIDs){
        Set<UserEntity> members = new HashSet<>();
        for (int i : membersIDs){
            members.add(userDao.findUserById(i));
        }
        return members;
    }
    //transforma o set de id's em entidades de user
    public Set<UserEntity> listMembersDtoToEntity(Set<ProjectMemberDto> member){
        return member.stream().map(userDto -> userDao.findUserById(userDto.getId())).collect(Collectors.toSet());
    }
    public Set<ProjectMemberDto> listUserEntityToMemberDto(Set<UserEntity> user){
        return user.stream().map(this :: ConvertUserEntityToProjectMembers).collect(Collectors.toSet());
    }
    // gerar token para email

    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }
    @Transactional
    private boolean userIsValid (UserEntity u){
        if (u == null){
            return false;
        }
        return userDao.findUserById(u.getId()) != null;
    }

    //para que é que isto aqui está seu burro (estou a falar para mim)
    private SelectProjectMembersDto convertUserEntToMemberAvailable(UserEntity user){
        SelectProjectMembersDto member = new SelectProjectMembersDto();
        member.setId(user.getId());
        member.setName(user.getFirstName() + " " + user.getLastName());
        member.setNickname(user.getNickname());
        member.setEmail(user.getEmail());
        return member;
    }
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
        //aqui é que tem de se usar o null pointer exception?
        return null;
    }

    /**
     * Edits the profile of a user.
     *
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
    //remove interest and skill
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
    @Transactional
    public void removeSkillFromUser(int userID, int interestID){
        UserEntity u = userDao.findUserById(userID);
        if (userIsValid(u)){
            SkillEntity s = skillService.getSKillByID(interestID);
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
    @Transactional
    public void leaveProject(int userID, int projectID){
        UserEntity u = userDao.findUserById(userID);
        if(u != null){
            ProjectEntity p = projectService.getProjectEntityByID(projectID);
            if (p != null){
                //fazer aqui verificação de que se user for manager nao pode sair
                p.getProjectMembers().remove(u);
                u.getProjects().remove(p);
                userDao.merge(u);
                userDao.flush();
            }
        }
    }
    @Transactional
    public void joinProject(int userID, int projectID){
        UserEntity u = userDao.findUserById(userID);
        if(u != null){
            ProjectEntity p = projectService.getProjectEntityByID(projectID);
            if (p != null){
                //aqui tem de haver uma verificação se o user recebeu um convite para se juntar ao projecto
                p.getProjectMembers().add(u);
                u.getProjects().add(p);
                userDao.merge(u);
                userDao.flush();
            }
        }
    }
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






