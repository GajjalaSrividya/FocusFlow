package com.focusflow.focusflow.service;

import com.focusflow.focusflow.model.Task;
import com.focusflow.focusflow.model.TaskPrimaryKey;
import com.focusflow.focusflow.model.UserLookup;
import com.focusflow.focusflow.repository.TaskRepository;
import com.focusflow.focusflow.repository.UserLookupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserLookupRepository userLookupRepository;



    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getTasksForUser(UUID userId) {
        return taskRepository.findByKeyUserId(userId);
    }

    public List<Task> getTasksForUserByDate(UUID userId, LocalDate date) {
        return taskRepository.findByKeyUserIdAndKeyTaskDate(userId, date);
    }

    public void deleteTask(TaskPrimaryKey key) {
        taskRepository.deleteById(key);
    }

    public List<Task> getPendingTasksForDate(UUID userId, LocalDate taskDate) {
    List<Task> tasks = taskRepository.findByKeyUserIdAndKeyTaskDate(userId, taskDate);
    return tasks.stream()
                .filter(task -> !task.isCompleted())
                .toList();
}


    public List<Task> getPendingTasksForWeek(String username) {
        UserLookup lookup = userLookupRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID userId = UUID.fromString(lookup.getUserId());

        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6); 

        return taskRepository.findPendingTasksForWeek(userId, today, endOfWeek);
    }

    public void markTaskAsCompleted(UUID userId, LocalDate taskDate, String taskName) {
        TaskPrimaryKey key = new TaskPrimaryKey(userId, taskDate, taskName);
        Task task = taskRepository.findById(key)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(true);
        taskRepository.save(task);
    }

    public Task updateTask(UUID userId, LocalDate taskDate, String oldTaskName, String newTaskName, boolean completed) {
        TaskPrimaryKey oldKey = new TaskPrimaryKey(userId, taskDate, oldTaskName);
        if (!taskRepository.existsById(oldKey)) {
            throw new RuntimeException("Task not found");
        }

        taskRepository.deleteById(oldKey);

        TaskPrimaryKey newKey = new TaskPrimaryKey(userId, taskDate, newTaskName);
        Task newTask = new Task(newKey, completed);
        return taskRepository.save(newTask);
    }
    public List<Task> getTasksForUpcomingWeek(UUID userId) {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6);
        return taskRepository.findByKeyUserIdAndKeyTaskDateBetween(userId, today, endOfWeek);
    }


}
