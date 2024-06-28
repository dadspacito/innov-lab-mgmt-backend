package api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.engine.jdbc.ReaderInputStream;
import service.TaskService;

//@Path("/tasks")
public class TaskResource {
    /**
     * que paths é que esta resource precisa?
     * criar task- precisa de verificar se o user que a está a criar é um user válido na DB e válido dentro do projeto
     * apagar task- mesmo principio do acima
     * receber tasks:
     * -ver validade do user como entity na DB
     * -ver validade do user como pertencente ao projeto
     * -filtrar se calhar tasks que ja estao completas
     * -retornar tasks associados a certo user
     * -retornar tasks associados a projeto com o respetivo state
     * -retornar task por id
     * -retornar tasks associados a user por projeto
     * -retornar tasks associados a user no geral
     *
     */
/*    @EJB
    private TaskService taskService;

    //tem de receber um user token? ID de criador?path de criação de task?
    @GET
    Produces(MediaType.APPLICATION_JSON)
    public Response createNewTask(@HeaderParam()) */

}
