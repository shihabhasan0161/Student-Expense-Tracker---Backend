package com.studentexpensetracker.studentexpensetracker.repo;

import com.studentexpensetracker.studentexpensetracker.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends MongoRepository<IncomeEntity, String> {
    List<IncomeEntity> findByProfileIdOrderByDateDesc(String profileId);
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(String profileId);
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

    @Query(value = "{ 'profileId': ?0, 'date': { $gte: ?1, $lte: ?2 }, 'name': { $regex: ?3, $options: 'i' } }")
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameRegex(
            String profileId,
            LocalDate startDate,
            LocalDate endDate,
            String nameRegex,
            Sort sort
    );

    List<IncomeEntity> findByProfileIdAndDateBetween(
            String profileId,
            LocalDate startDate,
            LocalDate endDate
    );
}
