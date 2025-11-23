package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.UserPermissionsResponseDTO;
import com.tedioinfernal.tedioapp.entity.Permission;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.entity.UserPermission;
import com.tedioinfernal.tedioapp.repository.PermissionRepository;
import com.tedioinfernal.tedioapp.repository.UserPermissionRepository;
import com.tedioinfernal.tedioapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Transactional
    public UserPermissionsResponseDTO assignPermissionsToUser(Long userId, List<String> permissionNames) {
        log.info("Assigning permissions to user ID: {} - Permissions: {}", userId, permissionNames);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Se a lista estiver vazia, remove todas as permissões
        if (permissionNames == null || permissionNames.isEmpty()) {
            log.info("Removing all permissions from user ID: {}", userId);
            userPermissionRepository.deleteByUserId(userId);
            return getUserPermissions(userId);
        }

        // Busca as permissões atuais do usuário
        List<UserPermission> currentPermissions = userPermissionRepository.findByUserId(userId);
        List<String> currentPermissionNames = currentPermissions.stream()
                .map(up -> up.getPermission().getName())
                .collect(Collectors.toList());

        // Adiciona apenas as permissões que ainda não existem
        for (String permissionName : permissionNames) {
            if (!currentPermissionNames.contains(permissionName)) {
                Permission permission = permissionRepository.findByName(permissionName)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada: " + permissionName));

                UserPermission userPermission = UserPermission.builder()
                        .user(user)
                        .permission(permission)
                        .build();

                userPermissionRepository.save(userPermission);
                log.info("Permission '{}' added to user ID: {}", permissionName, userId);
            } else {
                log.info("Permission '{}' already exists for user ID: {}, skipping", permissionName, userId);
            }
        }

        log.info("Permissions assigned successfully to user ID: {}", userId);
        return getUserPermissions(userId);
    }

    @Transactional(readOnly = true)
    public UserPermissionsResponseDTO getUserPermissions(Long userId) {
        log.info("Fetching permissions for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<UserPermission> userPermissions = userPermissionRepository.findByUserId(userId);

        List<String> permissions = userPermissions.stream()
                .map(up -> up.getPermission().getName())
                .collect(Collectors.toList());

        return UserPermissionsResponseDTO.builder()
                .userId(user.getId())
                .userName(user.getNome())
                .userEmail(user.getEmail())
                .permissions(permissions)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        log.info("Fetching all available permissions");
        return permissionRepository.findAll();
    }
}
