package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.dto.ExpenseDTO;
import com.studentexpensetracker.studentexpensetracker.entity.CategoryEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ExpenseEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import com.studentexpensetracker.studentexpensetracker.repo.CategoryRepository;
import com.studentexpensetracker.studentexpensetracker.repo.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profileId(profile.getId())
                .categoryId(category.getId())
                .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity entity) {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(entity.getCategoryId(), currentProfile.getId())
                .orElse(null);
        return ExpenseDTO.builder()
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
