package com.focusflow.focusflow.repository;

import com.focusflow.focusflow.model.Task;
import com.focusflow.focusflow.model.TaskPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends CassandraRepository<Task, TaskPrimaryKey> {

    List<Task> findByKeyUserId(UUID userId);

    List<Task> findByKeyUserIdAndKeyTaskDate(UUID userId, LocalDate taskDate);

    List<Task> findByKeyUserIdAndCompleted(UUID userId, boolean completed);
    @Query("SELECT * FROM tasks WHERE user_id = ?0 AND task_date >= ?1 AND task_date <= ?2 AND completed = false ALLOW FILTERING")
    List<Task> findPendingTasksForWeek(UUID userId, LocalDate startDate, LocalDate endDate);
    List<Task> findByKeyUserIdAndKeyTaskDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
}
