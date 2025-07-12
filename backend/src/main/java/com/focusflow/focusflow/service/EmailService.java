package com.focusflow.focusflow.service;

import com.focusflow.focusflow.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTaskEmail(String toEmail, List<Task> tasks) {
        String subject = "📝 Your Tasks for Today - FocusFlow";

        String body = tasks.stream()
                .map(task -> "- " + task.getKey().getTaskName()) 
                .collect(Collectors.joining("\n"));

        String content = "Hello!\n\nHere are your tasks for today:\n\n"
                + body
                + "\n\nStay productive!\n– Team FocusFlow";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

}
