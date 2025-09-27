package com.studentexpensetracker.studentexpensetracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "expenses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EnableMongoAuditing
public class ExpenseEntity {
    private String id;
    private String name;
    private String icon;
    private LocalDate date;
    private double amount;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private String categoryId;
    private String profileId;
}
