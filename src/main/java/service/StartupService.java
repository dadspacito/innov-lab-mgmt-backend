package service;

import dao.*;
import entity.*;

import enums.MaterialType;
import enums.ProjectState;
import enums.SkillType;
import jakarta.ejb.EJB;
import jakarta.ejb.Startup;
import jakarta.ejb.Singleton;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.annotation.PostConstruct;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;


@Singleton
@Startup
public class StartupService {

     @EJB
     private UserDao userDao;

    @EJB
    private SystemVariableDao systemVariableDao;
    @EJB
    private SessionTokenDao sessionTokenDao;
    @EJB
    private WorkplaceDao workplaceDao;

    @EJB
    private InterestDao interestDao;

    @EJB
    private SkillDao skillDao;

    @EJB
    private MaterialDao materialDao;
    @EJB private ProjectDao projectDao;
    private boolean initializationComplete = false;



    @PostConstruct
    public void init() {

    // verificar se existe user admin, se não existir é porque tem de ser criado tudo

     if (userDao.findUserByNickname("admin") == null) {


         // criar variáveis de sistema

         String[] workplaceNames = {"COIMBRA", "LISBOA", "PORTO", "VISEU", "TOMAR", "VILA REAL"};
         for (String name : workplaceNames) {
             WorkplaceEntity workplaceEntity = new WorkplaceEntity(name);
             workplaceDao.persist(workplaceEntity);
             workplaceDao.flush();
         }


         String[] systemVariableNames = {"session_timeout", "max_people_per_project"};
         String [] systemVariableValues = {"15", "4"};

         for (int i = 0; i < systemVariableNames.length; i++) {
             SystemVariableEntity systemVariableEntity = new SystemVariableEntity(systemVariableNames[i], Integer.parseInt(systemVariableValues[i]));
             systemVariableDao.persist(systemVariableEntity);
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

        SkillEntity skillEntity = new SkillEntity("C", SkillType.SOFTWARE);
        InterestEntity interestEntity = new InterestEntity("Ballet");
        // não é obrigatorio por causa do cascade.
        //interestDao.persist(interestEntity);
      //  skillDao.persist(skillEntity);
        UserEntity userEntity = userDao.findUserByNickname("admin");
        userEntity.getSkills().add(skillEntity);
        userEntity.getInterests().add(interestEntity);
        userDao.merge(userEntity);


        System.out.println("StartupService initialized.");
        createSkills();
        createInterests();
        createUsers();
        createMaterials();
        initializationComplete = true;
        //createProject(1);

        // NOTA. criar bean para start com criação de vários objetos






}

    @Transactional
    private void createSkills(){
        SkillEntity[] skills = {
                new SkillEntity("Python", SkillType.SOFTWARE),
                new SkillEntity("Java", SkillType.SOFTWARE),
                new SkillEntity("Scrum Master", SkillType.KNOWLEDGE),
                new SkillEntity("Project management", SkillType.KNOWLEDGE),
                new SkillEntity("Embedded systems", SkillType.HARDWARE),
                new SkillEntity("MatLab", SkillType.TOOLS),
                new SkillEntity("Machine Learning Specialist", SkillType.SOFTWARE),
                new SkillEntity("Circuit Architecture", SkillType.HARDWARE),
                new SkillEntity("Agile methodology", SkillType.KNOWLEDGE),
                new SkillEntity("Soldering", SkillType.HARDWARE)
        };
        for (SkillEntity skill : skills){
            skillDao.persist(skill);
            skillDao.flush();

        }

    }
    @Transactional
    private void createInterests(){
        InterestEntity[] interests={
                new InterestEntity("Software architecture"),
                new InterestEntity("Space exploration"),
                new InterestEntity("Industry 4.0"),
                new InterestEntity("Machine learning"),
                new InterestEntity("Education"),
                new InterestEntity("Nature and environment"),
                new InterestEntity("game development"),
                new InterestEntity("Sustainable food production"),
                new InterestEntity("Renewable energies"),
        };
        for (InterestEntity interest : interests){
            interestDao.persist(interest);
            interestDao.flush();


        }
    }
    @Transactional
    public void createMaterials() {
        MaterialEntity[] materials = new MaterialEntity[]{
                new MaterialEntity("Arduino Uno", "Arduino", MaterialType.COMPONENT,
                        "A microcontroller board for beginners.",
                        1001, "Arduino Inc.", 1234567890, 5,
                        "Used for prototyping."),

                new MaterialEntity("Raspberry Pi 4", "Raspberry Pi Foundation", MaterialType.COMPONENT,
                        "A small single-board computer.",
                        1002, "Raspberry Pi Org.", 1234567891, 5,
                        "Ideal for small projects."),

                new MaterialEntity("OLED Display", "Adafruit", MaterialType.COMPONENT,
                        "A 128x64 pixel OLED display.",
                        1003, "Adafruit Industries", 1234567892, 5,
                        "Used for displaying text and graphics."),

                new MaterialEntity("Temperature Sensor", "DHT11", MaterialType.COMPONENT,
                        "A basic temperature and humidity sensor.",
                        1004, "DHT Electronics", 1234567893, 5,
                        "Commonly used in weather stations."),

                new MaterialEntity("Step Motor", "NEMA", MaterialType.COMPONENT,
                        "A high precision step motor.",
                        1005, "NEMA Motors", 1234567894, 5,
                        "Used in precise motion control applications."),

                new MaterialEntity("LiPo Battery", "Turnigy", MaterialType.RESOURCE,
                        "A rechargeable lithium polymer battery.",
                        1006, "Turnigy", 1234567895, 5,
                        "Used for powering portable devices."),

                new MaterialEntity("Breadboard", "Elegoo", MaterialType.RESOURCE,
                        "A solderless prototyping board.",
                        1007, "Elegoo", 1234567896, 5,
                        "Essential for prototyping circuits."),

                new MaterialEntity("Jumper Wires", "Dupont", MaterialType.RESOURCE,
                        "Male to male jumper wires.",
                        1008, "Dupont", 1234567897, 5,
                        "Used for connecting components on a breadboard."),

                new MaterialEntity("Multimeter", "Fluke", MaterialType.RESOURCE,
                        "A digital multimeter for measuring voltage, current, and resistance.",
                        1009, "Fluke Corporation", 1234567898, 5,
                        "Essential tool for troubleshooting circuits."),

                new MaterialEntity("Soldering Kit", "Weller", MaterialType.RESOURCE,
                        "A complete soldering toolkit.",
                        1010, "Weller Tools", 1234567899, 5,
                        "Includes a soldering iron, solder, and accessories.")
        };

        // Persist each material entity using the materialDao
        for (MaterialEntity material : materials) {
            materialDao.persist(material);
            materialDao.flush();


        }
    }


    @Transactional
    private void createUsers(){
        InterestEntity interestSpace = interestDao.findInterestByName("Space exploration");
        InterestEntity interestNature = interestDao.findInterestByName("Nature and environment");
        InterestEntity interestML=interestDao.findInterestByName("Machine learning");
        InterestEntity interestIndustry=interestDao.findInterestByName("Industry 4.0");
        InterestEntity interestEducation =interestDao.findInterestByName("Education");

        SkillEntity skillPython = skillDao.findSkillByName("Python");
        SkillEntity skillJava = skillDao.findSkillByName("Java");
        SkillEntity skillScrum = skillDao.findSkillByName("Scrum Master");
        SkillEntity skillProjectManagement = skillDao.findSkillByName("Project management");
        SkillEntity skillMatlab = skillDao.findSkillByName("MatLab");
        UserEntity[] users = {
                createUser("alice", "Alice", "Smith", "alice@innovlabmgmt.com", "PORTO", skillPython, interestSpace),
                createUser("bob", "Bob", "Johnson", "bob@innovlabmgmt.com", "LISBOA", skillProjectManagement, interestEducation),
                createUser("charlie", "Charlie", "Brown", "charlie@innovlabmgmt.com", "VISEU", skillMatlab, interestNature),
                createUser("diana", "Diana", "Prince", "diana@innovlabmgmt.com", "TOMAR", skillScrum, interestIndustry),
                createUser("eve", "Eve", "Polastri", "eve@innovlabmgmt.com", "VILA REAL", skillJava, interestML)
        };
        for (UserEntity user : users) {
            userDao.persist(user);
            userDao.flush();


        }
    }
    private UserEntity createUser(String nickname, String firstName, String lastName, String email, String workplaceLocation, SkillEntity skill, InterestEntity interest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(nickname);
        userEntity.setPassword(BCrypt.hashpw("password", BCrypt.gensalt())); // Define a default password
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setAvatar("https://commons.wikimedia.org/wiki/File:Default_avatar_profile.jpg"); // Placeholder avatar URL
        userEntity.setAdmin(false);
        userEntity.setConfirmed(true);
        userEntity.setActive(true);
        userEntity.setPublicProfile(true);
        userEntity.setWorkplace(workplaceDao.findWorkplaceByLocation(workplaceLocation));
        userEntity.getSkills().add(skill);
        userEntity.getInterests().add(interest);
        return userEntity;
    }
  //o problema desta classe é que esta a iniciar ao mesmo tempo que as outras
    //tem de iniciar só apos
    @Transactional
    private void createProject(int managerID){
        if (!initializationComplete) {
            throw new IllegalStateException("initialization steps not completed");
        }
                ProjectEntity startupProject = new ProjectEntity();
                startupProject.setName("Projecto teste");
                startupProject.setDescription("projeto inicial para ver se as tabelas estão funcionais");
                startupProject.setStartDate(LocalDateTime.now());
                startupProject.setEndDate(LocalDateTime.now().plusDays(2));
                startupProject.setProjectWorkplace(workplaceDao.findWorkplaceByLocation("COIMBRA"));
                startupProject.setProjectState(ProjectState.PLANNING);
                UserEntity manager = userDao.findUserById(managerID);
                startupProject.addProjectManager(manager);
                for (UserEntity member : userDao.getUserByWorkplace(1)) {//os membros escolhidos vem num array e é preciso correr o ciclo e adiciona-los ao projeto
                    startupProject.addMember(member);
                }
                for (SkillEntity skill : skillDao.findAll()) {
                    startupProject.addSkill(skill);
                }
                for (InterestEntity interest : interestDao.findAll()) {
                    startupProject.addInterest(interest);
                }
                for (MaterialEntity material : materialDao.findAll()) {
                    startupProject.addMaterial(material);
                }
                projectDao.persist(startupProject);
            }

}
