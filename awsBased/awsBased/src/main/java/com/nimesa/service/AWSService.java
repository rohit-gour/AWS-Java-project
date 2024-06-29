package com.nimesa.service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.nimesa.Repository.S3BucketRepository;
import com.nimesa.Repository.EC2InstanceRepository;
import com.nimesa.Repository.JobRepository;
import com.nimesa.dto.EC2InstanceDTO;
import com.nimesa.dto.JobStatusDTO;
import com.nimesa.dto.S3BucketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AWSService {

    @Autowired
    private AmazonEC2 amazonEC2;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EC2InstanceRepository ec2InstanceRepository;

    @Autowired
    private S3BucketRepository s3BucketRepository;

    /*
    DiscoverServices(List<String> services)
    * */
    @Transactional
    public CompletableFuture<Void> discoverEC2Instances() {
        return CompletableFuture.runAsync(() -> {
            try {
                DescribeInstancesRequest request = new DescribeInstancesRequest();
                DescribeInstancesResult result = amazonEC2.describeInstances(request);
                for (Reservation reservation : result.getReservations()) {
                    for (com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances()) {
                        EC2InstanceDTO ec2Instance = new EC2InstanceDTO();
                        ec2Instance.setInstanceId(instance.getInstanceId());
                        ec2InstanceRepository.save(ec2Instance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to discover EC2 instances", e);
            }
        });
    }

    @Transactional
    public CompletableFuture<Void> discoverS3Buckets() {
        return CompletableFuture.runAsync(() -> {
            try {
                List<Bucket> buckets = amazonS3.listBuckets();
                for (Bucket bucket : buckets) {
                    S3BucketDTO s3Bucket = new S3BucketDTO();
                    s3Bucket.setBucketName(bucket.getName());
                    s3BucketRepository.save(s3Bucket);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to discover S3 buckets", e);
            }
        });
    }

    @Transactional
    public UUID getS3BucketObjects(String bucketName) {
        JobStatusDTO job = new JobStatusDTO();
        job.setStatus("In Progress");
        JobStatusDTO savedJob = jobRepository.save(job);

        CompletableFuture.runAsync(() -> {
            try {
                ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
                ListObjectsV2Result result;
                do {
                    result = amazonS3.listObjectsV2(req);

                    result.getObjectSummaries().forEach(s3Object -> {
                        S3BucketDTO s3BucketDTO = new S3BucketDTO();
                        s3BucketDTO.setBucketName(s3Object.getKey());
                        s3BucketRepository.save(s3BucketDTO);
                    });
                    req.setContinuationToken(result.getNextContinuationToken());
                } while (result.isTruncated());

                job.setStatus("Success");
                jobRepository.save(job);
            } catch (Exception e) {
                e.printStackTrace();
                job.setStatus("Failed");
                jobRepository.save(job);
            }
        });

        return savedJob.getId();
    }

    public long getS3BucketObjectCount(String bucketName) {
        return s3BucketRepository.findByBucketName(bucketName).size();
    }

    public List<String> getS3BucketObjectLike(String bucketName, String pattern) {
        return s3BucketRepository.findByBucketName(bucketName).stream()
                .filter(s3Object -> s3Object.getBucketName().contains(pattern))
                .map(S3BucketDTO::getBucketName)
                .collect(Collectors.toList());
    }

}
