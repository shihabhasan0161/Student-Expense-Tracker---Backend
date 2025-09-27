package com.studentexpensetracker.studentexpensetracker.repo;

import com.studentexpensetracker.studentexpensetracker.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<ExpenseEntity, String> {
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(String profileId);
    List<ExpenseEntity> findTop5ByOrderByDateDesc(String profileId);
    @Query(value = "{ 'profileId' : ?0 }", fields = "{ 'amount' : 1, '_id' : 0 }")
    Optional<Double> findTotalExpenseBYProfileId(String profileId);
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            String profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );
    List<ExpenseEntity> findByProfileIdAndDateBetween(
            String profileId,
            LocalDate startDate,
            LocalDate endDate
    );
}
