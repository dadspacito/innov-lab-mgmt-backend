package service;

import dao.ProjectDao;
import dto.BasicProjectDto;
import dto.DetailedProjectDto;
import entity.ProjectEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;

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
        //tem que ser validado aqui pelos atributos do projeto (ver se não são null)

        projectDao.persist(convertProjectDtoToEntity(p));
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
        //esta função retorna um user entity to user service atraves de um project member dto
        pEnt.setManager((userService.defineManager(p.getProjectManager())));
        pEnt.setProjectWorkplace(p.getWorkplace());
        //aqui retorna um set. Poderá dar problemas mais tarde.
        pEnt.setInterests(interestService.projectInterests(p.getProjectInterests()));
        pEnt.setSkills(skillService.returnProjectSkills(p.getProjectSkills()));
        pEnt.setMaterials(materialService.returnProjectMaterials(p.getProjectMaterials()));
        pEnt.setTasks(taskService.getTasksFromProject(p.getProjectTasks()));
        pEnt.setProjectMembers(userService.returnMembersEntity(p.getProjectMembers()));
        return pEnt;
    }
    /**
     * que verificações privadas se podem por aqui?
     * pode ver se existe o interesse, a skill, o material e o manager
     */





}
