package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
    
    List<Path> findByIntegrationId(Long integrationId);
}
