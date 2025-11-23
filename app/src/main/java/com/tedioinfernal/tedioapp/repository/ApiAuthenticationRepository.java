package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.ApiAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiAuthenticationRepository extends JpaRepository<ApiAuthentication, Long> {
    List<ApiAuthentication> findByOwnerId(Long ownerId);
    Optional<ApiAuthentication> findByNome(String nome);
}
