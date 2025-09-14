package com.studentexpensetracker.studentexpensetracker.repo;

import com.studentexpensetracker.studentexpensetracker.entity.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    List<CategoryEntity> findByProfileId(String profileId);

    Optional<CategoryEntity> findByIdAndProfileId(String id, String profileId);

    List<CategoryEntity> findByTypeAndProfileId(String type, String profileId);

    Boolean existsByNameAndProfileId(String name, String profileId);
}