/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.resources;
import herbert.task.app.blog.application.controllers.SessionController;
import herbert.task.app.blog.application.models.Token;
import herbert.task.app.blog.application.models.Users;
import herbert.task.app.blog.application.services.BlogService;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.LocalDateTime;

/**
 *
 * @author HerbertSekpey
 */

 
@Path("/auth") 
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    @Inject
    BlogService blog;
    
    @Inject 
    SessionController login;
    
    
   @Path("/verify-token")
   @GET 
   public Response verifyToken(@QueryParam("token") String token){
     
    Token verifyToken =(Token) blog.findByField(Token.class,"token", token);
    if(verifyToken == null){
        return Response.status(Response.Status.NOT_FOUND).entity("Token does not exist").build();
    }
    if(!verifyToken.isValid()){
         return Response.status(Response.Status.CONFLICT).entity("Token is invalid. Link has already been used").build();
    }
    if(verifyToken.getExpiryTime().isBefore(LocalDateTime.now()) || !verifyToken.isValid()){
        verifyToken.setValid(false);
        blog.update(verifyToken);
        return Response.status(Response.Status.CONFLICT).entity("Token has expired").build();
    }
    //HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    verifyToken.setValid(false);
    verifyToken = (Token) blog.update(verifyToken);
 
    Users user = (Users) blog.find(Users.class,verifyToken.getUser().getId());
    if(!user.isAccountVerified()){
        user.setAccountVerified(true);
        blog.update(user);
        URI targetUri = UriBuilder.fromUri("/Blog-Application/login.xhtml").build();
          System.out.println("User SIGNED UP");
        return Response.seeOther(targetUri).build();
    }
    else{
        login.setUser(user); 
        URI targetUri = UriBuilder.fromUri("/Blog-Application/dashboard.xhtml").build();
           System.out.println("User lOGGED IN");
        return Response.seeOther(targetUri).build();   
    }
   }
    
    
    
    
    
}
