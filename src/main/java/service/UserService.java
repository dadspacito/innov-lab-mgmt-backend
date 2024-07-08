package service;


import dao.SessionTokenDao;
import dao.UserDao;
import dao.SystemVariableDao;
import dao.WorkplaceDao;

import dto.*;

import entity.ProjectEntity;
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
 * faltam funções de conversão de user entity em dto
 * falta mudar perfis para privados/publicos
 * falta adicionar ou remover interesses e skills de users;
 * falta criar uma lista de convites que os users tem quando se convidam para projeto
 *  -convite tem de ter um id para quando o user aceita poder entrar no projecto especifico
 *
 */

@Stateless
public class UserService {

    @EJB
    private UserDao userDao;
    @EJB private WorkplaceService workplaceService;

    @EJB
    private SystemVariableDao systemVariableDao;

    @EJB
    private WorkplaceDao workplaceDao;
    @EJB private SessionTokenDao sessionTokenDao;
    @EJB private ProjectService projectService;

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
        // criar user entity com dados de dto
        // associar dados do DTO ao user
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        user.setAvatar("https://commons.wikimedia.org/wiki/File:Default_avatar_profile.jpg");
        user.setWorkplace(workplaceDao.find(userDto.getWorkplaceId()));
        // a verificação ja foi feita, nao vai dar erro.
     //   user.setCreatedAt(LocalDateTime.now());
        user.setActive(false);
        user.setConfirmed(false);
        user.setEmailToken(generateNewToken());
        user.setEmailTokenExpires(LocalDateTime.now().plusHours(1));
        String verificationLink = "http://localhost:3000/users/activations/" + user.getEmailToken();
        EmailSender.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationLink);
        userDao.persist(user);
         return UserState.CREATED;

    }

    // função que vai ativar user
    //

    public boolean confirmUser(String emailToken) {
        UserEntity user = userDao.findUserByEmailToken(emailToken);
        if (user == null) {
            System.out.println("entrei no activate user no user service");
            return false;
        }
        user.setConfirmed(true);
        user.setEmailToken(null);
        user.setEmailTokenExpires(null);
        userDao.merge(user);
        return true;
    }
    // obter user pelo token (IMPORTANTE. função só chamada EM RESOURCE após validação de token, logo user existe)
    //aqui tem de vir o header dto

    public UserEntity getUserByToken(String token) {
        return sessionTokenDao.findUserBySessionToken(token);
    }
    //get user list (only active) - para adicionar ao projeto
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






    //função que envia mail ao user para seguir para o link de reset password
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
        System.out.println("entrou aqui");
        System.out.println(newPassword);
        System.out.println(user.getPassword());
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        System.out.println(user.getPassword());
        user.setEmailToken(null);
        user.setEmailTokenExpires(null);
        userDao.merge(user);
        System.out.println(user.getPassword());
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
    //so retorna os membros do projeto quando é feita a query
    public List<ProjectMemberDto> returnProjectMembers(int workplaceID) {
        // Check if the workplace exists
        if (workplaceDao.getWorkplaceByID(workplaceID) != null) {
            // Fetch and convert users to ProjectMemberDto using Streams
            return userDao.getUserByWorkplace(workplaceID).stream()
                    .map(this::ConvertUserEntityToProjectMembers)
                    .collect(Collectors.toList());
        }
        // Return an empty list if the workplace does not exist
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
    private boolean userIsValid (UserEntity u){
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
            //Retorna uma lista de projectos em que o user está inserido.
            //userProfile.setIsInProject(projectService.listUserProfileProjectsDto(u.getProjects()));
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
}






