package entity;

import enums.MaterialType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="Material")
//queries: tem de devolver a lista inteira de materiais
//devolve os materiais que tenham o ID do projeto
//devolver materiais por projeto id
@NamedQueries({
        @NamedQuery(name = "Material.findMaterialByID", query = "SELECT m FROM MaterialEntity m WHERE m.id= :id"),
        @NamedQuery(name = "Material.findMaterialByName", query ="select m from MaterialEntity m where m.name = :name"),
        @NamedQuery(name ="Material.getAllMaterials", query = "select m from MaterialEntity m order by m.id asc")

})
public class MaterialEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;
    @Column(name = "brand", nullable = false, unique = true, updatable = true)
    private String brand;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = true)
    private MaterialType type;
    @Column(name="description", nullable = true, updatable = true)
    private String description;
    @Column(name ="serialNumber", nullable = false, updatable = true)
    private int serialNumber;
    @Column (name = "supplier", nullable = false, updatable = true)
    private String supplier;
    @Column (name = "supplierContact", nullable = false, updatable = true)
    private int supplierContact;
    @Column (name = "quantity", nullable = false, updatable = true)
    private int quantity;
    @Column(name = "observations", nullable = true, updatable = true)
    private String observations;
    //esta relaçõa é one to many com os projetos


    public MaterialEntity() {}

    public MaterialEntity(String name, String brand, MaterialType type, String description, int serialNumber, String supplier, int supplierContact, int quantity, String observations) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.description = description;
        this.serialNumber = serialNumber;
        this.supplier = supplier;
        this.supplierContact = supplierContact;
        this.quantity = quantity;
        this.observations = observations;
    }

    @ManyToOne
    @JoinColumn(name="project_id")//foreign key do projeto
    private ProjectEntity project;


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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public MaterialType getType() {
        return type;
    }

    public void setType(MaterialType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(int supplierContact) {
        this.supplierContact = supplierContact;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialEntity that = (MaterialEntity) o;
        return id == that.id; // Compare based on ID for persisted entities
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use ID for hash code
    }
}
