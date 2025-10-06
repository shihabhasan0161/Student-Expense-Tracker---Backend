package com.studentexpensetracker.studentexpensetracker.repo;

import com.studentexpensetracker.studentexpensetracker.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends MongoRepository<IncomeEntity, String> {
    List<IncomeEntity> findByProfileIdOrderByDateDesc(String profileId);
    List<IncomeEntity> findTop5ByOrderByDateDesc(String profileId);
    @Aggregation(pipeline = {
            "{ $match: { profileId: ?0 } }",
            "{ $group: { _id: null, total: { $sum: \"$amount\" } } }"
    })
    AmountTotal findTotalIncomeBYProfileId(String profileId);
    interface AmountTotal {
        Double getTotal();
    }
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            String profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );
    List<IncomeEntity> findByProfileIdAndDateBetween(
            String profileId,
            LocalDate startDate,
            LocalDate endDate
    );
}
