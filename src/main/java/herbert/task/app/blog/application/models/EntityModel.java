/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
/**
 *
 * @author HerbertSekpey
 */
@MappedSuperclass
public abstract class EntityModel implements Serializable{
    
    
    @Id
    @Column(name = "ID")
    private String id;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
  
    @PrePersist
    public void init(){
        setId(UUID.randomUUID().toString().replace("-", ""));
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

    
    @PreUpdate
    public void init2(){
     setUpdatedAt(LocalDateTime.now());   
    }
    
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

   
    
    
}
