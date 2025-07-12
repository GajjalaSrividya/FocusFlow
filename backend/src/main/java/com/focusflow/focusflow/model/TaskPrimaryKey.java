package com.focusflow.focusflow.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass 
public class TaskPrimaryKey {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    private UUID userId;

    @PrimaryKeyColumn(name = "task_date", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    private LocalDate taskDate;
    
    @PrimaryKeyColumn(name = "task_name", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String taskName;
}
