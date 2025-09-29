package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.dto.ExpenseDTO;
import com.studentexpensetracker.studentexpensetracker.dto.IncomeDTO;
import com.studentexpensetracker.studentexpensetracker.dto.RecentTransactionDTO;
import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashBoardData(){
        ProfileEntity profile = profileService.getCurrentUser();
        Map<String, Object> dashBoardData = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.get5LatestIncomes();
        List<ExpenseDTO> latestExpenses = expenseService.get5LatestExpenses();
        List<RecentTransactionDTO> recentTransactionsList = Stream.concat(
                latestIncomes.stream().map(income ->
                    RecentTransactionDTO.builder()
                            .id(income.getId())
                            .profileId(profile.getId())
                            .icon(income.getIcon())
                            .name(income.getName())
                            .amount(income.getAmount())
                            .date(income.getDate() != null ? income.getDate() : LocalDate.now())
                            .createdAt(income.getCreatedAt())
                            .updatedAt(income.getUpdatedAt())
                            .type("income")
                            .build()
                ),
                latestExpenses.stream().map(expense ->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate() != null ? expense.getDate() : LocalDate.now())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()
                )
                )
        .sorted((a, b) -> {
            int compare = b.getDate().compareTo(a.getDate());
            if (compare == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return compare;
        }).collect(Collectors.toList());
        dashBoardData.put("totalBalance", incomeService.getTotalIncomeAmount() - (expenseService.getTotalExpenseAmount()));
        dashBoardData.put("totalIncome", incomeService.getTotalIncomeAmount());
        dashBoardData.put("totalExpense", expenseService.getTotalExpenseAmount());
        dashBoardData.put("recent5incomes", latestIncomes);
        dashBoardData.put("recent5expenses", latestExpenses);
        dashBoardData.put("recentTransactions", recentTransactionsList.stream().limit(5).collect(Collectors.toList()));
        return dashBoardData;
    }
}
