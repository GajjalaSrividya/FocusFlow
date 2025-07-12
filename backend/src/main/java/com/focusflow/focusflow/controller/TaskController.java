package com.focusflow.focusflow.controller;

import com.focusflow.focusflow.model.Task;
import com.focusflow.focusflow.model.TaskPrimaryKey;
import com.focusflow.focusflow.model.UserLookup;
import com.focusflow.focusflow.service.TaskService;
import com.focusflow.focusflow.repository.UserLookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.focusflow.focusflow.dto.TaskUpdateRequest;
import com.focusflow.focusflow.dto.TaskKeyDTO;
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserLookupRepository userLookupRepository;

    private UUID getUserIdFromUserDetails(UserDetails userDetails) {
        String username = userDetails.getUsername();
        UserLookup lookup = userLookupRepository.findById(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return UUID.fromString(lookup.getUserId());
    }

    @PostMapping("/create")
    public Task createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Task task) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        task.getKey().setUserId(userId);
        return taskService.saveTask(task);
    }

    @GetMapping("/history")
    public List<Task> getAllTasksByUser(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        return taskService.getTasksForUser(userId);
    }

    @GetMapping("/date/{date}")
    public List<Task> getTasksByDate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String date) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        LocalDate taskDate = LocalDate.parse(date);
        return taskService.getTasksForUserByDate(userId, taskDate);
    }

    @DeleteMapping("/delete/{date}/{taskName}")
    public void deleteTask(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable String date,
        @PathVariable String taskName
    ) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        LocalDate taskDate = LocalDate.parse(date);
        TaskPrimaryKey key = new TaskPrimaryKey(userId, taskDate, taskName);
        taskService.deleteTask(key);
    }

    @GetMapping("/pending")
    public List<Task> getPendingTasks(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        LocalDate today = LocalDate.now();
        return taskService.getPendingTasksForDate(userId, today);
    }
    @GetMapping("/pending/week")
    public List<Task> getPendingTasksForWeek(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.getPendingTasksForWeek(userDetails.getUsername());
    }

    @PutMapping("/complete/{date}/{taskName}")
    public void markTaskAsCompleted(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable String date,
        @PathVariable String taskName
    ) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        LocalDate taskDate = LocalDate.parse(date);
        taskService.markTaskAsCompleted(userId, taskDate, taskName);
    }

    @PutMapping("/update")
    public Task updateTask(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestBody TaskUpdateRequest request) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        TaskKeyDTO keyDto = request.getKey();

        return taskService.updateTask(
            userId,
            LocalDate.parse(keyDto.getTaskDate()),
            keyDto.getOldTaskName(),
            keyDto.getTaskName(),
            request.isCompleted()
        );
    }



    @GetMapping("/week")
    public List<Task> getTasksForUpcomingWeek(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserIdFromUserDetails(userDetails);
        return taskService.getTasksForUpcomingWeek(userId);
    }


}
