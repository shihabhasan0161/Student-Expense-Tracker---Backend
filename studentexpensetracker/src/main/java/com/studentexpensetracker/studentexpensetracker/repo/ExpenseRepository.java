package com.studentexpensetracker.studentexpensetracker.repo;

import com.studentexpensetracker.studentexpensetracker.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<ExpenseEntity, String> {
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(String profileId);
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(String profileId);
    @Aggregation(pipeline = {
            "{ $match: { profileId: ?0 } }",
            "{ $group: { _id: null, total: { $sum: \"$amount\" } } }"
    })
    ExpenseRepository.AmountTotal findTotalExpenseBYProfileId(String profileId);
    interface AmountTotal {
        Double getTotal();
    }
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            String profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    @Query(value = "{ 'profileId': ?0, 'date': { $gte: ?1, $lte: ?2 }, 'name': { $regex: ?3, $options: 'i' } }")
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameRegex(
            String profileId,
            LocalDate startDate,
            LocalDate endDate,
            String nameRegex,
            Sort sort
    );

    List<ExpenseEntity> findByProfileIdAndDateBetween(
            String profileId,
            LocalDate startDate,
            LocalDate endDate
    );
}
