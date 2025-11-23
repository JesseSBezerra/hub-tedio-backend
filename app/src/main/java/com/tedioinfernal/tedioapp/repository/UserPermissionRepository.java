package com.tedioinfernal.tedioapp.repository;

import com.tedioinfernal.tedioapp.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    
    @Query("SELECT up FROM UserPermission up JOIN FETCH up.permission WHERE up.user.id = :userId")
    List<UserPermission> findByUserId(@Param("userId") Long userId);
    
    void deleteByUserIdAndPermissionId(Long userId, Long permissionId);
    
    boolean existsByUserIdAndPermissionId(Long userId, Long permissionId);
    
    void deleteByUserId(Long userId);
}
