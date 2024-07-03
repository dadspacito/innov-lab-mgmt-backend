/*
package service;

import dao.*;
import entity.*;
import enums.SkillType;
import enums.MaterialType;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

*/
/**
 * este serviço tem como objetivo fornecer dados à app para inicializar com alguns dados
 * criar 5 users
 * criar 5 skills
 * criar 5 materials
 * criar 5 interesses
 *//*


@Stateless
public class BaseDataService {
    */
/**
     * inicializa aqui a create skills, interests, users e materials
     * precisa dos dao's para fazer persist
     *//*

    @EJB
    private InterestDao interestDao;
    @EJB
    private SkillDao skillDao;
    @EJB
    private MaterialDao materialDao;
    @EJB
    private UserDao userDao;
    @EJB
    private WorkplaceDao workplaceDao;

    public void initializeBaseData(){
        createMaterials();
        createInterests();
        createSkills();
        createUsers();
    }
    */
/*@Transactional
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

        }
    }*//*

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


}
*/
