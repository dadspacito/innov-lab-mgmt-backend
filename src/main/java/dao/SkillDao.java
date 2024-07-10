
package dao;

import entity.SkillEntity;
import entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class SkillDao extends AbstractDao<SkillEntity> {

        private static final long serialVersionUID = 1L;

        public SkillDao() {
            super(SkillEntity.class);
        }

        public SkillEntity findSkillByName(String name){
            try {
                return em.createNamedQuery("Skill.findSkillByName", SkillEntity.class).setParameter("name", name).getSingleResult();
            }
            catch(NoResultException e) {
                return null;
            }
        }
        public SkillEntity findSkillByID(int id){
            try {
                return em.createNamedQuery("Skill.findSkillByID", SkillEntity.class).setParameter("id", id).getSingleResult();
            }
            catch (NoResultException e){
                System.err.println("Skill does not exist in the Database");
                return null;
            }
        }
        public Set<SkillEntity> getAllSkillsOrdered(){
            try{
                List<SkillEntity> skills = em.createNamedQuery("Skill.findAllSkills", SkillEntity.class).getResultList();
                return new HashSet<>(skills);
            }
            catch (NoResultException e){
                return null;
            }
        }
}
