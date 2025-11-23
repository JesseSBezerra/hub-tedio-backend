package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.Evolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvolutionRepository extends JpaRepository<Evolution, Long> {
    
    Optional<Evolution> findByNome(String nome);
    
    boolean existsByNome(String nome);
    
    List<Evolution> findByOwnerId(Long ownerId);
}
