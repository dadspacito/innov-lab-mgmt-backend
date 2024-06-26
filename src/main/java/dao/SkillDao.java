
package dao;

import entity.SkillEntity;
import entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;

@Stateless
public class SkillDao extends AbstractDao<SkillEntity> {

        private static final long serialVersionUID = 1L;

        public SkillDao() {
            super(SkillEntity.class);
        }

}
