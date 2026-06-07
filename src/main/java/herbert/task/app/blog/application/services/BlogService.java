/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.services;

import herbert.task.app.blog.application.models.EntityModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 *
 * @author HerbertSekpey
 */

@ApplicationScoped
@Transactional 
public class BlogService{
    
    @PersistenceContext(unitName = "blog_app_unit")
    EntityManager em;
    
    
    public void save(EntityModel model){
        em.persist(model);
    }
    
    public Object find(Class e, String id){
        Object findModel = em.find(e, id);
        if(findModel == null){
          return null;   
        }
        return findModel;
    }
    
    public Object findByField(Class e, String field, String value){
       try{
            Object output = em.createQuery("SELECT u FROM "+ e.getSimpleName()+" u WHERE u."+field +" = :value "  ,Object.class)
                         .setParameter("value", value)
                         .getSingleResult();
            
            return output;
       }
       
       catch(NoResultException ex){
          return null; 
       }
    }
    
    public Object findByFields(Class e, String field1, String field2, String value1, String value2 ){
       try{
            Object output = em.createQuery("SELECT u FROM "+ e.getSimpleName()+" u WHERE u."+field1 +" = :value1 AND u."+field2 +" = :value2",Object.class)
                         .setParameter("value1", value1)
                         .setParameter("value2", value2)
                         .getSingleResult();
            
            return output;
       }
       
       catch(NoResultException ex){
          return null; 
       }
    }
    
    
    
    public Object update(EntityModel e){
      Object updated = em.merge(e);
      return updated;
    }
    
    public void delete(EntityModel e){
        em.remove(e);
    }
    
    
    
    
    
    
}
