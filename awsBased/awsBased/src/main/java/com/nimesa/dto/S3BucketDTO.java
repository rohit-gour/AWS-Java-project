package com.nimesa.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
public class S3BucketDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String bucketName;

    // getters and setters

    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getId(){
        return this.id;
    }
    public void setBucketName(String bucketName){
        this.bucketName = bucketName;
    }
    public String getBucketName(){
        return this.bucketName;
    }
}