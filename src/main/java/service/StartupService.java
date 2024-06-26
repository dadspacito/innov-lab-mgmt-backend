package service;

import dao.*;
import entity.UserEntity;
import entity.WorkplaceEntity;
import entity.SystemVariableEntity;
import entity.SessionTokenEntity;
import entity.SkillEntity;
import entity.InterestEntity;
import enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Startup;
import jakarta.ejb.Singleton;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.annotation.PostConstruct;


import java.time.LocalDateTime;


@Singleton
@Startup
public class StartupService {

     @EJB
    UserDao userDao;

    @EJB
    SystemVariableDao systemVariableDao;

    @EJB
    WorkplaceDao workplaceDao;

    @EJB
    SessionTokenDao sessionTokenDao;

    @EJB
    InterestDao interestDao;

    @EJB
    SkillDao skillDao;



    @PostConstruct
    public void init() {

    // verificar se existe user admin, se não existir é porque tem de ser criado tudo

     if (userDao.findUserByNickname("admin") == null) {

         // criar variáveis de sistema

         String[] systemVariableNames = {"session_timeout", "max_people_per_project"};
         String [] systemVariableValues = {"15", "4"};

         for (int i = 0; i < systemVariableNames.length; i++) {
             SystemVariableEntity systemVariableEntity = new SystemVariableEntity(systemVariableNames[i], Integer.parseInt(systemVariableValues[i]));
             systemVariableDao.persist(systemVariableEntity);
         }


         // criar workplaces

         String[] workplaceNames = {"COIMBRA", "LISBOA", "PORTO", "VISEU", "TOMAR", "VILA REAL"};

         for (String name : workplaceNames) {
             WorkplaceEntity workplaceEntity = new WorkplaceEntity(name);
             workplaceDao.persist(workplaceEntity);
         }


         UserEntity userEntity = new UserEntity();

       //    criar user admin

        userEntity.setNickname("admin");
        userEntity.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        userEntity.setFirstName("Admin");
        userEntity.setLastName("Admin");
        userEntity.setEmail("admin@innovlabmgmt.com");
        userEntity.setAvatar("https://commons.wikimedia.org/wiki/File:Default_avatar_profile.jpg");
        //userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setAdmin(true);
        userEntity.setConfirmed(true);
        userEntity.setActive(true);
        userEntity.setPublicProfile(false);
        userEntity.setWorkplace(workplaceDao.findWorkplaceByLocation("COIMBRA"));
        userDao.persist(userEntity);


    }

     // reset de tokens de sessão (falta reset de tokens de email)
        sessionTokenDao.deleteAll();


         // definir um token de sessão para o user admin, SEMPRE VÁLIDO PARA EFEITOS DE TESTE
        // COMENTÁRIO MANTER ISTO APENAS EM DESENVOLVIMENTO

      if (sessionTokenDao.findUserBySessionToken("admin") == null) {
          SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
          sessionTokenEntity.setToken("admin");
         sessionTokenEntity.setTokenTimeout(LocalDateTime.now().plusYears(1));
          sessionTokenEntity.setUser(userDao.findUserByNickname("admin"));
          sessionTokenDao.persist(sessionTokenEntity);
      }

      // teste de criar skills e interesses

        SkillEntity skillEntity = new SkillEntity("Java", SkillType.SOFTWARE);
        InterestEntity interestEntity = new InterestEntity("Yoga");
        // não é obrigatorio por causa do cascade.
        //interestDao.persist(interestEntity);
      //  skillDao.persist(skillEntity);
        UserEntity userEntity = userDao.findUserByNickname("admin");
        userEntity.getSkills().add(skillEntity);
        userEntity.getInterests().add(interestEntity);
        userDao.merge(userEntity);


        System.out.println("StartupService initialized.");


        // NOTA. criar bean para start com criação de vários objetos




}



}
