package com.nimesa.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
public class JobStatusDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String status;


    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public UUID getId() {
        return this.id;
    }
}