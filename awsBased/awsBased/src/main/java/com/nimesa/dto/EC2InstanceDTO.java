package com.nimesa.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Entity
public class EC2InstanceDTO {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String instanceId = "123";

    // getters and setters

    public void setId(UUID id) {
        this.id = id;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public UUID getId(){
        return this.id;
    }
    public String getInstanceId(){
        return this.instanceId;
    }
}