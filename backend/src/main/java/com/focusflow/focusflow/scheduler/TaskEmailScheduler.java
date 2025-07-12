package com.focusflow.focusflow.scheduler;

import com.focusflow.focusflow.model.Task;
import com.focusflow.focusflow.model.User;
import com.focusflow.focusflow.repository.UserRepository;
import com.focusflow.focusflow.service.EmailService;
import com.focusflow.focusflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class TaskEmailScheduler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void sendDailyTaskEmails() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            UUID userId = user.getUserId();
            String email = user.getEmail();

            List<Task> todayTasks = taskService.getTasksForUserByDate(userId, LocalDate.now());

            if (!todayTasks.isEmpty()) {
                emailService.sendTaskEmail(email, todayTasks);
            }
        }
    }
}
