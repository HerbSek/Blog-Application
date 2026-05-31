/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.controllers;

import herbert.task.app.blog.application.interfaces.ControllerInterface;
import herbert.task.app.blog.application.models.Users;
import herbert.task.app.blog.application.services.BlogService;
import herbert.task.app.blog.application.util.MessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Context;

/**
 *
 * @author HerbertSekpey
 */



@RequestScoped
@Named("authController")
public class AuthController implements ControllerInterface{
    
    @Inject 
    BlogService blogService;
    
    @Inject 
    MessageUtil message;
    
    
    private Users user;
   
  @PostConstruct 
  public void init(){  
      user = new Users();
      
  }
    
    
   public void saveUser(){
       save();
       clear();
       System.out.println("User saved and fields cleared !!!");
   } 
    
    
    @Override
    public void save(){
        blogService.save(user);
    };
    
    
     @Override
    public void update(){
        
    };
    
    
     @Override
    public void delete(){
        
    };
    
    
     @Override
    public void clear(){
       user= new Users(); 
    };  

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
}
