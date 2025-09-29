package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.dto.ExpenseDTO;
import com.studentexpensetracker.studentexpensetracker.entity.CategoryEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ExpenseEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import com.studentexpensetracker.studentexpensetracker.repo.CategoryRepository;
import com.studentexpensetracker.studentexpensetracker.repo.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    // Create new expense
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        ExpenseEntity entity = toEntity(dto, currentProfile, category);
        ExpenseEntity savedEntity = expenseRepository.save(entity);
        return toDTO(savedEntity);
    }

    // Get total expense for current user for current month/based on start date and end date
    public List<ExpenseDTO> getCurrentMonthExpenses() {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetween(currentProfile.getId(), startOfMonth, endOfMonth);
        return expenses.stream().map(this::toDTO).toList();
    }

    //delete expense by id
    public void deleteExpenseById(String expenseId) {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        ExpenseEntity existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!existingExpense.getProfileId().equals(currentProfile.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.delete(existingExpense);
    }

    //Get latest 5 expenses for current user
    public List<ExpenseDTO> get5LatestExpenses() {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        List<ExpenseEntity> expenses = expenseRepository.findTop5ByOrderByDateDesc(currentProfile.getId());
        return expenses.stream().map(this::toDTO).toList();
    }

    //Get total expense amount for current user
    public Double getTotalExpenseAmount() {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        return expenseRepository.findTotalExpenseBYProfileId(currentProfile.getId()).orElse(0.0);
    }

    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .profileId(profile.getId())
                .categoryId(category.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
