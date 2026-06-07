/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.controllers;

import herbert.task.app.blog.application.interfaces.ControllerInterface;
import herbert.task.app.blog.application.models.Token;
import herbert.task.app.blog.application.models.Users;
import herbert.task.app.blog.application.services.BlogService;
import herbert.task.app.blog.application.util.EmailUtil;
import herbert.task.app.blog.application.util.MessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.core.Context;
import java.time.LocalDateTime;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 *
 * @author HerbertSekpey
 */



@RequestScoped
@Named("authController")
public class AuthController implements ControllerInterface{
    
    @Inject 
    SessionController sessionUser;
    
    @Inject 
    BlogService blogService;
    
    @Inject 
    MessageUtil message;
    
    @Inject 
    EmailUtil sendEmail;
 
    private Users user;
    
    private String userNameInput;
   
  @PostConstruct 
  public void init(){  
      user = new Users();
      
  }
   
  
  public void signUp() throws MessagingException, IOException{ 
     
      if(sessionUser.getUser() != null){
          FacesContext.getCurrentInstance().getExternalContext().redirect("/Blog-Application/dashboard.xhtml?faces-redirect=true");
          return;
      }
     
    String baseUrl = baseUrl();
    Users findUser = (Users) blogService.findByFields(Users.class, "userName", "email", user.getUserName(), user.getEmail());
     if(findUser != null && findUser.isAccountVerified()){
         System.out.println("User already exists !!!. Try logging in");
         return;
     }
    if(findUser != null && !findUser.isAccountVerified()){
        String newTokenCred = java.util.UUID.randomUUID().toString();
        findUser.getToken().setToken(newTokenCred);
        findUser.getToken().setExpiryTime(LocalDateTime.now().plusMinutes(5));
        findUser.getToken().setValid(true);
        findUser = (Users) blogService.update(findUser);
        String tokenUrl = baseUrl + "/v1/auth/verify-token" + "?token=" + findUser.getToken().getToken();
        sendEmail.sendVerificationEmail(findUser.getEmail(), tokenUrl);
        redirectToCheckEmail(findUser.getEmail(), "signup");
        return;
     }
    
    System.out.println(baseUrl);
    Token token = new Token();
    String newTokenCred = java.util.UUID.randomUUID().toString();
    token.setToken(newTokenCred);
    token.setExpiryTime(LocalDateTime.now().plusMinutes(5));
    token.setValid(true);
    token.setUser(user);
    user.setToken(token);
    save(); 
    
    String tokenUrl = baseUrl + "/v1/auth/verify-token" + "?token=" + token.getToken();
    sendEmail.sendVerificationEmail(user.getEmail(), tokenUrl);
    redirectToCheckEmail(user.getEmail(), "signup");
  }
  
 public void login() throws MessagingException, IOException{ 
     
     if(sessionUser.getUser() != null){
          FacesContext.getCurrentInstance().getExternalContext().redirect("/Blog-Application/dashboard.xhtml?faces-redirect=true");
          return;
      }
     
     if(userNameInput==null){
         System.out.println(" Field is empty ");
         return; 
     }
     
     Users findUser = (Users) blogService.findByField(Users.class, "userName", userNameInput);
     if(findUser == null){
         System.out.println("No UserName like this exists");
         return;
     }
     if(!findUser.isAccountVerified()){
       System.out.println("User Account has not been verified. Please try and sign up");
         return;  
     }
    String baseUrl = baseUrl();
    System.out.println(baseUrl);
    String newTokenCred = java.util.UUID.randomUUID().toString();
    findUser.getToken().setToken(newTokenCred);
    findUser.getToken().setExpiryTime(LocalDateTime.now().plusMinutes(5));
    findUser.getToken().setValid(true);
    findUser = (Users) blogService.update(findUser);
    
    String tokenUrl = baseUrl + "/v1/auth/verify-token" + "?token=" + findUser.getToken().getToken();
    sendEmail.sendLoginEmail(findUser.getEmail(), tokenUrl);
    redirectToCheckEmail(findUser.getEmail(), "login");
   }
 
 public void logout() throws IOException{
     sessionUser.setUser(null);
     FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
     FacesContext.getCurrentInstance().getExternalContext().redirect("/Blog-Application/login.xhtml");
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

    public String getUserNameInput() {
        return userNameInput;
    }

    public void setUserNameInput(String userNameInput) {
        this.userNameInput = userNameInput;
    }


    private void redirectToCheckEmail(String email, String context) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("sentToEmail", email);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("emailContext", context);
        clear();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Blog-Application/check-email.xhtml");
    }

    public String baseUrl(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        String baseUrl = java.net.URI.create(request.getRequestURL().toString())
                             .resolve(request.getContextPath())
                             .toString();
        
        return baseUrl;
    }
    
    
}
