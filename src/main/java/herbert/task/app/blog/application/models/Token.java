/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 *
 * @author HerbertSekpey
 */

@Entity
@Table(name = "TOKENS")
public class Token extends EntityModel{

    
    @Column(name = "TOKEN")
    private String token;
     
    @Column(name = "VALID")
    private boolean valid; 
     
    @Column(name = "EXPIRY_TIME")
    private LocalDateTime expiryTime; 

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Users user;
    
    
//    @PrePersist
//    public void init3(){
//        setToken(UUID.randomUUID().toString());
//    }
    
    
     public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
   
    
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
