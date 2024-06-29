package com.nimesa.controller;

import com.nimesa.Repository.EC2InstanceRepository;
import com.nimesa.Repository.JobRepository;
import com.nimesa.Repository.S3BucketRepository;
import com.nimesa.dto.JobStatusDTO;
import com.nimesa.service.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/api")
public class AWSController {

    @Autowired
    private AWSService awsService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EC2InstanceRepository ec2InstanceRepository;

    @Autowired
    private S3BucketRepository s3BucketRepository;

    @PostMapping("/discoverServices")
    public UUID discoverServices(@RequestBody List<String> services) {
        JobStatusDTO job = new JobStatusDTO();
        job.setStatus("In Progress");
        JobStatusDTO savedJob = jobRepository.save(job);

        CompletableFuture<Void> ec2Future = null;
        CompletableFuture<Void> s3Future = null;

        if (services.contains("EC2")) {
            ec2Future = awsService.discoverEC2Instances();
        }

        if (services.contains("S3")) {
            s3Future = awsService.discoverS3Buckets();
        }

        CompletableFuture.allOf(ec2Future, s3Future).thenRun(() -> {
            job.setStatus("Success");
            jobRepository.save(job);
        }).exceptionally(ex -> {
            job.setStatus("Failed");
            jobRepository.save(job);
            return null;
        });

        return savedJob.getId();
    }

    @GetMapping("/getJobResult/{jobId}")
    public String getJobResult(@PathVariable UUID jobId) {
        return jobRepository.findById(jobId).map(JobStatusDTO::getStatus).orElse("Job not found");
    }

    @GetMapping("/getDiscoveryResult/{service}")
    public Object getDiscoveryResult(@PathVariable String service) {
        if ("S3".equalsIgnoreCase(service)) {
            return s3BucketRepository.findAll();
        } else if ("EC2".equalsIgnoreCase(service)) {
            return ec2InstanceRepository.findAll();
        }
        return "Service not supported";
    }
    @PostMapping("/getS3BucketObjects/{bucketName}")
    public UUID getS3BucketObjects(@PathVariable String bucketName) {
        return awsService.getS3BucketObjects(bucketName);
    }

    @GetMapping("/getS3BucketObjectCount/{bucketName}")
    public long getS3BucketObjectCount(@PathVariable String bucketName) {
        return awsService.getS3BucketObjectCount(bucketName);
    }

    @GetMapping("/getS3BucketObjectlike/{bucketName}/{pattern}")
    public List<String> getS3BucketObjectLike(@PathVariable String bucketName, @PathVariable String pattern) {
        return awsService.getS3BucketObjectLike(bucketName, pattern);
    }
}
