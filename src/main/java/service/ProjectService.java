package service;

import dto.BasicProjectDto;
import dto.DetailedProjectDto;
import entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;

@Stateless
public class ProjectService {
    @Inject
    InterestService interestService;
    @Inject UserService userService;

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


    }

    /**
     * Private methods
     * convert entity to basic project dto and detailed project dto e vice versa
     *
     * estes métodos depois podem ser chamados dentro de um ciclo para retornar as listas
     *metodo privado é ver role do user no projeto para determinar permissões
     * role manager ser true = poder executar funções de adicionar ou tirar pessoas do projeto
     * pensar na estruturação dos passos para a criação do
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
        pEnt.addWorkplaceToProject(p.getWorkplace());

        //é preciso todos os services de interests e tasks que convertam as entidades em dtos e vice versa

        return pEnt;

    }





}
