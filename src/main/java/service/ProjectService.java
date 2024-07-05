package service;

import dao.ProjectDao;
import dao.TaskDao;
import dto.BasicProjectDto;
import dto.DetailedProjectDto;
import dto.TaskDto;
import entity.ProjectEntity;
import entity.TaskEntity;
import entity.WorkplaceEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class ProjectService {
    @Inject
    private InterestService interestService;
    @Inject private UserService userService;
    @Inject private TaskService taskService;
    @Inject private SkillService skillService;
    @Inject private MaterialService materialService;
    @EJB
    private ProjectDao projectDao;
    @EJB
    private TaskDao taskDao;
    @EJB WorkplaceService workplaceService;

    /**
     * que EJB's é que são precisos aqui?
     * que lógica é que esta classe tem de ter
     * métodos publicos de ligação ao frontend, transactional:
     *  -criar projeto
     *  -editar projeto (permissões para editar projetos e afins (métodos privados))
     *  -cancelar/apagar projeto
     *  -retornar lista de projetos consoante queries
     * metodos privados: conversão entidade em dto, verificação de credenciais de acesso
     * retorno de DTO de projeto publico (user nao logged in), de DTO privado (logged in)
     * os métodos de conversão tem de vir dos EJBS
     */

    /**
     * são preciso duas transacionais, uma para criar o projeto ate ao ponto saved (pré incluição materiais)
     *
     */
    @Transactional
    public void createNewProject(DetailedProjectDto p){
        try{
            //creates and persist initial project
            ProjectEntity projectEntity = convertProjectDtoToEntity(p);
            projectDao.persist(projectEntity);
            projectDao.flush();
            //creates initial task and associates the id of the project
            TaskDto initialTaskDto = taskService.createPresentationTask(p);
            TaskEntity initialTaskEntity = taskService.convertTaskDtoToEntity(initialTaskDto);
            initialTaskEntity.setProject(projectEntity);
            taskDao.persist(initialTaskEntity);
            taskDao.flush();
            //associate the task with the project
            projectEntity.getTasks().add(initialTaskEntity);
            projectDao.merge(projectEntity);
            projectDao.flush();
        }
        catch(IllegalArgumentException e){
            System.err.println("error setting the project");
            e.printStackTrace();
        }
    }
    public List<ProjectEntity> getProjects(){
        try{
           return projectDao.getAllProjectsOrdered();
        }
        catch (NoResultException e){
            System.err.println("No projects were found");
            return null;
        }
    }

    /**
     * Private methods
     * convert entity to basic project dto and detailed project dto e vice versa
     *
     * estes métodos depois podem ser chamados dentro de um ciclo para retornar as listas
     *metodo privado é ver role do user no projeto para determinar permissões
     * role manager ser true = poder executar funções de adicionar ou tirar pessoas do projeto
     * pensar na estruturação dos passos para a criação do
     * aqui tem de poder criar materiais e adicioná-los ao projeto
     * o mesmo para as skills, tasks, e interesses
     * estes serviços são puxados dos serviços das entidades expecificas e adicionadas ao projeto
     */

    //para fazer persist de um novo projeto

    private ProjectEntity convertProjectDtoToEntity (DetailedProjectDto p){
        ProjectEntity pEnt = new ProjectEntity();
        pEnt.setName(p.getName());
        pEnt.setDescription(p.getDescription());
        pEnt.setStartDate(p.getStartDate());
        pEnt.setEndDate(p.getEndDate());
        pEnt.setProjectState(p.getProjectState());
        //esta função retorna um user entity to user service atraves de um project member dto
        pEnt.setManager((userService.defineManager(p.getProjectManager())));
        //workplace é por id
        pEnt.setProjectWorkplace(workplaceService.getWorkplaceByID(p.getWorkplace()));
        //aqui retorna um set. Poderá dar problemas mais tarde.
        //tem de ir buscar os interesses primeiro e so depois fazer set
        pEnt.getInterests().addAll(interestService.projectInterests(p.getProjectInterests()));
        //skills é por id
        pEnt.getSkills().addAll(skillService.returnProjectSkills(p.getProjectSkills()));
        //materiais é por id
        pEnt.getMaterials().addAll(materialService.returnProjectMaterials(p.getProjectMaterials()));
        //as tasks funcionam de maneira diferente porque nao existem de maneira preemptiva na base de dados para se
        //adicionar ao projeto. Os id's das tasks servem para retorno da task, mas quando se cria um projeto este não vem com
        pEnt.setProjectMembers(userService.returnMembersEntity(p.getProjectMembers()));
        return pEnt;
    }
    /**
     * que verificações privadas se podem por aqui?
     * pode ver se existe o interesse, a skill, o material e o manager
     */





}
