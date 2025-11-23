package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
