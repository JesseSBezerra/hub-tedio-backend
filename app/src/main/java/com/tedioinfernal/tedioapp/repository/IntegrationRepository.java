package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.Integration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationRepository extends JpaRepository<Integration, Long> {
    
    Optional<Integration> findByNome(String nome);
    
    List<Integration> findByOwnerId(Long ownerId);
}
