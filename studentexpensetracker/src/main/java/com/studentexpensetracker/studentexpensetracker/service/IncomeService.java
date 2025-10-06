package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.dto.ExpenseDTO;
import com.studentexpensetracker.studentexpensetracker.dto.IncomeDTO;
import com.studentexpensetracker.studentexpensetracker.entity.CategoryEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ExpenseEntity;
import com.studentexpensetracker.studentexpensetracker.entity.IncomeEntity;
import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import com.studentexpensetracker.studentexpensetracker.repo.CategoryRepository;
import com.studentexpensetracker.studentexpensetracker.repo.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    // Create new income
    public IncomeDTO createIncome(IncomeDTO dto) {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(dto.getCategoryId(), currentProfile.getId()).orElseThrow(() -> new RuntimeException("Category not found"));
        IncomeEntity entity = toEntity(dto, currentProfile, category);
        IncomeEntity savedEntity = incomeRepository.save(entity);
        return toDTO(savedEntity);
    }

    // Get total income for current user for current month/based on start date and end date
    public List<IncomeDTO> getCurrentMonthIncomes() {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdAndDateBetween(currentProfile.getId(), startOfMonth, endOfMonth);
        return incomes.stream().map(this::toDTO).toList();
    }

    //delete income by id
    public void deleteIncomeById(String incomeId) {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        IncomeEntity existingIncome = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!existingIncome.getProfileId().equals(currentProfile.getId())) {
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepository.delete(existingIncome);
    }

    //Get latest 5 incomes for current user
    public List<IncomeDTO> get5LatestIncomes() {
        ProfileEntity currentProfile = profileService.getCurrentUser();
        List<IncomeEntity> incomes = incomeRepository.findTop5ByProfileIdOrderByDateDesc(currentProfile.getId());
        return incomes.stream().map(this::toDTO).toList();
    }

    //Get total income amount for current user
    public Double getTotalIncomeAmount() {
        ProfileEntity profile = profileService.getCurrentUser();
        return Optional
                .ofNullable(incomeRepository.findTotalIncomeBYProfileId(profile.getId()))
                .map(IncomeRepository.AmountTotal::getTotal)
                .orElse(0.0);
    }

    //filter incomes by date and keyword
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentUser();
        String regex = (keyword == null || keyword.isBlank())
                ? ".*"
                : ".*" + Pattern.quote(keyword) + ".*";

        List<IncomeEntity> incomes = incomeRepository
                .findByProfileIdAndDateBetweenAndNameRegex(profile.getId(), startDate, endDate, regex, sort);

        return incomes.stream().map(this::toDTO).toList();
    }

    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
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
