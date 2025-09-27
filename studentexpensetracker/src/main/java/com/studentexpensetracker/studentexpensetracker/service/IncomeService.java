package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.dto.IncomeDTO;
import com.studentexpensetracker.studentexpensetracker.entity.CategoryEntity;
import com.studentexpensetracker.studentexpensetracker.entity.IncomeEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import com.studentexpensetracker.studentexpensetracker.repo.CategoryRepository;
import com.studentexpensetracker.studentexpensetracker.repo.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profileId(profile.getId())
                .categoryId(category.getId())
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity) {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(entity.getCategoryId(), currentProfile.getId())
                .orElse(null);
        return IncomeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategoryId())
                .categoryName(category != null ? category.getName() : "Not Found")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
