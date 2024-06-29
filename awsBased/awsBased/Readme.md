# AWS-Based Spring Boot Application

## Overview

This Spring Boot application provides REST APIs to interact with AWS services (EC2 and S3). The application can discover EC2 instances and S3 buckets, and persist their information in a database. It also supports retrieving the discovery results and managing job statuses for the discovery processes.

## Prerequisites

1. **Java 17**: Ensure you have JDK 17 installed.
2. **MySQL Database**: Set up a MySQL database for the application to connect to.
3. **AWS Credentials**: Configure your AWS credentials to allow access to EC2 and S3 services.

## Configuration

**Project Structure**

# Controller

## AWSController.java: Handles HTTP requests and maps them to the service layer.

# Service

### AWSService.java: Contains the business logic for interacting with AWS services and managing discovery jobs.

# DTO

1. EC2InstanceDTO.java: Data transfer object for EC2 instances.
2. S3BucketDTO.java: Data transfer object for S3 buckets.
3. JobStatusDTO.java: Data transfer object for job statuses.

Repository
1. EC2InstanceRepository.java: JPA repository for EC2InstanceDTO.
2. S3BucketRepository.java: JPA repository for S3BucketDTO.
3. JobRepository.java: JPA repository for JobStatusDTO.

# Application Entry Point

AwsBasedApplication.java: Main class to bootstrap the Spring Boot application.
Dependencies
Spring Boot Starter Data JPA
Spring Boot Starter Web
AWS SDK for EC2 and S3
Lombok
MySQL Connector


Edit the `application.yml` file to set your MySQL database connection details and other properties:

```yaml
spring:
  server:
    port: 8081
  application:
    name: awsBased
  datasource:
    url: jdbc:mysql://localhost:3306/userdb
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update


```
REST APIs
1. DiscoverServices
   Endpoint: **/api/discoverServices**
   Method: POST
   Input: List of services (EC2 and/or S3)
   Output: JobId

Description: This API triggers the discovery of EC2 instances and S3 buckets asynchronously and returns a Job ID to track the discovery status.

Request Body:
{
"services": ["EC2", "S3"]
}

2. GetJobResult
   Endpoint: **/api/getJobResult/{jobId}**
   Method: GET
   Input: Job ID
   Output: Job Status (Success, In Progress, or Failed)

Description: This API returns the status of the discovery job for the given Job ID.

Response:
{
"status": "Success"
}

3. GetDiscoveryResult
   Endpoint: **/api/getDiscoveryResult/{service}**
   Method: GET
   Input: Service Name (EC2 or S3)
   Output: List of discovered EC2 instances or S3 buckets

Description: This API returns the list of discovered EC2 instances or S3 buckets based on the service name provided.

Response for EC2:
{
"instances": ["instance-id-1", "instance-id-2"]
}

4. GetS3BucketObjects
   Endpoint: **/api/getS3BucketObjects/{bucketName}**
   Method: POST
   Input: S3 Bucket Name
   Output: JobId

Description: This API triggers the discovery of objects within the specified S3 bucket and returns a Job ID to track the discovery status.

Response:
{
"jobId": "your-generated-job-id"
}


5. GetS3BucketObjectCount
   Endpoint: **/api/getS3BucketObjectCount/{bucketName}**
   Method: GET
   Input: S3 Bucket Name
   Output: Count of objects in the S3 bucket

Description: This API returns the count of objects within the specified S3 bucket.

Response:
{
"count": 1
}

6. GetS3BucketObjectLike
   Endpoint: **/api/getS3BucketObjectlike/{bucketName}/{pattern}**
   Method: GET
   Input: S3 Bucket Name and Pattern
   Output: List of object names matching the pattern

Description: This API returns a list of objects within the specified S3 bucket that match the given pattern.

Response:
{
"objects": ["object-name-1", "object-name-2"]
}