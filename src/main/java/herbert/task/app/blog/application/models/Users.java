/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 *
 * @author HerbertSekpey
 */
@Entity
@Table(name = "USERS")
public class Users extends EntityModel {
    
    
    @Column(name = "USERNAME", nullable=false, unique=true)
    private String userName;
     
    @Column(name = "EMAIL_ADDRESS", nullable=false, unique=true)
    private String email;
  
    
    @OneToOne(mappedBy = "user")
    private Token token;
    
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

   
  
  
    
}
