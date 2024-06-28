package dto;

import enums.MaterialType;

/**
 * •  Nome.
 * •  Marca.
 * •  Tipo (Componente ou Recurso).
 * •  Descrição.
 * •  Identificador (part number).
 * •  Fornecedor.
 * •  Contacto Fornecedor.
 * •  Quantidade.
 * •  Observações
 */

public class MaterialDto {
    private int id;
    private String name;
    private String brand;
    private MaterialType type;

    private String description;

    private int serialNumber;

    private String supplier;
    private int supplierContact;

    private int quantity;
    private String observations;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
