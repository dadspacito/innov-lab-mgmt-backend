package service;

import dao.*;
import dto.DataPDFDto;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

@Stateless
public class AdminService {
    //DAO's
    @EJB private InterestDao interestDao;
    @EJB private MaterialDao materialDao;
    @EJB private ProjectDao projectDao;
    @EJB private SkillDao skillDao;
    @EJB private UserDao userDao;
    @EJB private WorkplaceDao workplaceDao;

    //SERVICES
    @EJB private InterestService interestService;
    @EJB private MaterialService materialService;
    @EJB private ProjectService projectService;
    @EJB private SkillService skillService;
    @EJB private UserService userService;
    @EJB private WorkplaceService workplaceService;

    @Transactional
    public void createPDFDocument(){};


}
