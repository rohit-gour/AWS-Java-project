package com.nimesa.Repository;

import com.nimesa.dto.S3BucketDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface S3BucketRepository extends JpaRepository<S3BucketDTO, UUID> {
    List<S3BucketDTO> findByBucketName(String bucketName);
}
