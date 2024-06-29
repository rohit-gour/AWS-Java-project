package com.nimesa.Repository;

import com.nimesa.dto.JobStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<JobStatusDTO, UUID> {
}
