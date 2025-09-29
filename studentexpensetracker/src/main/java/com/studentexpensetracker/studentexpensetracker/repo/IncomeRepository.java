package com.studentexpensetracker.studentexpensetracker.repo;

import com.studentexpensetracker.studentexpensetracker.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends MongoRepository<IncomeEntity, String> {
    List<IncomeEntity> findByProfileIdOrderByDateDesc(String profileId);
    List<IncomeEntity> findTop5ByOrderByDateDesc(String profileId);
    @Query(value = "{ 'profileId' : ?0 }", fields = "{ 'amount' : 1, '_id' : 0 }")
    Optional<Double> findTotalIncomeBYProfileId(String profileId);
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
