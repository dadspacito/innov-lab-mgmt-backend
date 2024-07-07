package dto;

/**
 * esta classe define quem é o manager e que autorizações tem
 * tem de ser possivel passar de project member para project manager e vice versa
 * aqui tem de ficar a lógica
 */
public class ProjectManagerDto {
    private int id;
    private String name;
    private boolean isManager = true;
    private WorkplaceDto managerWorkplace;

    public ProjectManagerDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public WorkplaceDto getManagerWorkplace() {
        return managerWorkplace;
    }

    public void setManagerWorkplace(WorkplaceDto managerWorkplace) {
        this.managerWorkplace = managerWorkplace;
    }
}
