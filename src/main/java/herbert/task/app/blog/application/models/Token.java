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

/**
 *
 * @author HerbertSekpey
 */

@Entity
@Table(name = "TOKENS")
public class Token extends EntityModel{

    
    @Column(name = "TOKEN")
    private Long token;
     
    @Column(name = "EXPIRY")
    private boolean expired; 
     
    @Column(name = "EXPIRY_TIME")
    private LocalDateTime  expiryTime; 

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Users user;
    
    
     public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
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
