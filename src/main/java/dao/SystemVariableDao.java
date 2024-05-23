package dao;

import entity.SystemVariableEntity;
import jakarta.persistence.NoResultException;

import jakarta.ejb.Stateless;

@Stateless
public class SystemVariableDao extends AbstractDao<SystemVariableEntity> {

    private static final long serialVersionUID = 1L;

    public SystemVariableDao() {
        super(SystemVariableEntity.class);
    }

    public SystemVariableEntity findSystemVariableByName(String variableName) {
        try {
            return (SystemVariableEntity) em.createNamedQuery("SystemVariable.findVariableByName").setParameter("variableName", variableName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean updateSystemVariableValue(String variableName, int variableValue) {
        try {
            em.createNamedQuery("SystemVariable.updateVariableValue").setParameter("variableName", variableName).setParameter("variableValue", variableValue).executeUpdate();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }


}
