package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.*;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.enums.UserType;
import com.tedioinfernal.tedioapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createApplicationUser(ApplicationUserRequestDTO requestDTO) {
        log.info("Creating application user with email: {}", requestDTO.getEmail());
        
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .userType(UserType.APPLICATION)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Application user created successfully with ID: {}", savedUser.getId());
        
        return mapToResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO createIntegrationUser(IntegrationUserRequestDTO requestDTO) {
        log.info("Creating integration user with email: {}", requestDTO.getEmail());
        
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .userType(UserType.INTEGRATION)
                .clientId(requestDTO.getClientId())
                .clientSecret(passwordEncoder.encode(requestDTO.getClientSecret()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("Integration user created successfully with ID: {}", savedUser.getId());
        
        return mapToResponseDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return mapToResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return mapToResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, ApplicationUserRequestDTO requestDTO) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getEmail().equals(requestDTO.getEmail()) && 
            userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        user.setNome(requestDTO.getNome());
        user.setEmail(requestDTO.getEmail());
        
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return mapToResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .userType(user.getUserType())
                .clientId(user.getClientId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
