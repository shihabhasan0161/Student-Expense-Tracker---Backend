package com.studentexpensetracker.studentexpensetracker.service;

import com.studentexpensetracker.studentexpensetracker.entity.ProfileEntity;
import com.studentexpensetracker.studentexpensetracker.repo.ProfileRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // for logging
public class NotificationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${sbt.frontend.url}")
    private String frontendUrl;

    // commenting out for now
//    @Scheduled(cron = "0 0 22 * * *", zone = "EST") // Do this task Every day at 10 PM
    public void sendDailyBudgetReminder() {
        log.info("Task started: Sending daily budget reminders");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles) {
            String email = "Hi " + profile.getFullName() + ",\n\n"
                    + "This is a friendly reminder to add your daily income and expenses. "
                    + "You can log in to your account here: " + frontendUrl + "\n\n"
                    + "Best regards,\n"
                    + "Student Expense Tracker Team";
            emailService.sendEmail(profile.getEmail(), "Daily Reminder: Add your income and expenses", email);
        }
        log.info("Task completed: Daily budget reminders sent");

    }
}
