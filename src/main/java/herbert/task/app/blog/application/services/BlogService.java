/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.services;

import herbert.task.app.blog.application.models.EntityModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
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
    
    
    
    
    
    
    
    
    
    
}
