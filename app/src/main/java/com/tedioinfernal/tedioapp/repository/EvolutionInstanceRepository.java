package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvolutionInstanceRepository extends JpaRepository<EvolutionInstance, Long> {
    
    Optional<EvolutionInstance> findByInstanceName(String instanceName);
    
    Optional<EvolutionInstance> findByInstanceId(String instanceId);
    
    List<EvolutionInstance> findByEvolutionId(Long evolutionId);
    
    List<EvolutionInstance> findByUserId(Long userId);
    
    List<EvolutionInstance> findByStatus(String status);
    
    boolean existsByInstanceName(String instanceName);
}
