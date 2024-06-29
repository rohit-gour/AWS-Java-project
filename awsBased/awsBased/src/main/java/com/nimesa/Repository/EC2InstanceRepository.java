package com.nimesa.Repository;

import com.nimesa.dto.EC2InstanceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface EC2InstanceRepository extends JpaRepository<EC2InstanceDTO, UUID> {
}
