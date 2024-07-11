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

/**
 * Singleton startup service responsible for initializing the application with default data.
 */
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
    /**
     * Initialization method called after the singleton is created.
     * Populates the database with initial data
     */
    @PostConstruct
    public void init() {
        //checks the existence of a admin superuser
     if (userDao.findUserByNickname("admin") == null) {
         //populates the workplace tabçe
         String[] workplaceNames = {"COIMBRA", "LISBOA", "PORTO", "VISEU", "TOMAR", "VILA REAL"};
         for (String name : workplaceNames) {
             WorkplaceEntity workplaceEntity = new WorkplaceEntity(name);
             workplaceDao.persist(workplaceEntity);
             workplaceDao.flush();
         }

         //creates the variables for session timeout and max people in projects
         String[] systemVariableNames = {"session_timeout", "max_people_per_project"};
         String [] systemVariableValues = {"15", "4"};

         for (int i = 0; i < systemVariableNames.length; i++) {
             SystemVariableEntity systemVariableEntity = new SystemVariableEntity(systemVariableNames[i], Integer.parseInt(systemVariableValues[i]));
             systemVariableDao.persist(systemVariableEntity);
         }
         //creates a new admin "superuser"
         UserEntity userEntity = new UserEntity();
        userEntity.setNickname("admin");
        userEntity.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        userEntity.setFirstName("Admin");
        userEntity.setLastName("Admin");
        userEntity.setEmail("admin@innovlabmgmt.com");
        userEntity.setAvatar("https://commons.wikimedia.org/wiki/File:Default_avatar_profile.jpg");
        userEntity.setAdmin(true);
        userEntity.setConfirmed(true);
        userEntity.setActive(true);
        userEntity.setPublicProfile(false);
        userEntity.setWorkplace(workplaceDao.findWorkplaceByLocation("COIMBRA"));
        userDao.persist(userEntity);
        userDao.persist(userEntity);


    }
        sessionTokenDao.deleteAll();
      if (sessionTokenDao.findUserBySessionToken("admin") == null) {
          SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
          sessionTokenEntity.setToken("admin");
         sessionTokenEntity.setTokenTimeout(LocalDateTime.now().plusYears(1));
          sessionTokenEntity.setUser(userDao.findUserByNickname("admin"));
          sessionTokenDao.persist(sessionTokenEntity);
      }
        SkillEntity skillEntity = new SkillEntity("C", SkillType.SOFTWARE);
        InterestEntity interestEntity = new InterestEntity("Ballet");
        UserEntity userEntity = userDao.findUserByNickname("admin");
        userEntity.getSkills().add(skillEntity);
        userEntity.getInterests().add(interestEntity);
        userDao.merge(userEntity);


        System.out.println("StartupService initialized.");
        createSkills();
        createInterests();
        createUsers();
        createMaterials();
}
    /**
     * Creates default skills.
     */
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
    /**
     * Creates default interests.
     */
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
    /**
     * Creates default materials.
     */
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
        for (MaterialEntity material : materials) {
            materialDao.persist(material);
            materialDao.flush();
        }
    }

    /**
     * populates table with users
     */
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

    /**
     * Sets the parameters for the creation of default users in database when system starts
     * @param nickname
     * @param firstName
     * @param lastName
     * @param email
     * @param workplaceLocation
     * @param skill
     * @param interest
     * @return
     */
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
}
