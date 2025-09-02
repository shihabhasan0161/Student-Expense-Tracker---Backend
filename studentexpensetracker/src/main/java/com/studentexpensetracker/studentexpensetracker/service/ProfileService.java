package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.dto.ProfileDTO;
import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import com.studentexpensetracker.studentexpensetracker.repo.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ProfileDTO registerProfile (ProfileDTO profileDTO) {
        // Convert DTO to Entity
        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setActivationToken(UUID.randomUUID().toString());
        // Save Entity to DB
        profileEntity = profileRepository.save(profileEntity);

        // Send activation email

        String activationLink = "http://localhost:8080/api/v1/activate?token=" + profileEntity.getActivationToken();
        String subject = "Activate your Student Expense Tracker Account";
        String body = "Dear " + profileEntity.getFullName() + ",\n\n"
                + "Thank you for registering with Student Expense Tracker. Please click the link below to activate your account:\n"
                + activationLink + "\n\n";
        emailService.sendEmail(profileEntity.getEmail(), subject, body);

        // Convert saved Entity back to DTO
        return toDTO(profileEntity);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        // Convert DTO to Entity
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        // Convert Entity to DTO
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        ProfileEntity profileEntity = profileRepository.findByActivationToken(activationToken)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));

        profileEntity.setActive(true);
        profileRepository.save(profileEntity);
        return true;
    }
}
