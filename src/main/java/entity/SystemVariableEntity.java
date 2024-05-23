package entity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="SystemVariable")

@NamedQueries(
        {
                @NamedQuery(name = "SystemVariable.findVariableByName", query = "SELECT s FROM SystemVariableEntity s WHERE s.variableName = :variableName"),
                @NamedQuery(name = "SystemVariable.updateVariableValue", query = "UPDATE SystemVariableEntity s SET s.variableValue = :variableValue WHERE s.variableName = :variableName")
        }

)
public class SystemVariableEntity implements Serializable {

    // tabela para armazenar as variáveis de sistema modificaveis pelo admin
    // timeout de sessao e numero de pessoas maximo por projeto

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="variable_name", nullable = false, unique = true, updatable = true)
    private String variableName;

    @Column(name="variable_value", nullable = false, unique = false, updatable = true)
    private int variableValue;


    public SystemVariableEntity() {
    }

    public SystemVariableEntity(String variableName, int variableValue) {
        this.variableName = variableName;
        this.variableValue = variableValue;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public int getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(int variableValue) {
        this.variableValue = variableValue;
    }
}
