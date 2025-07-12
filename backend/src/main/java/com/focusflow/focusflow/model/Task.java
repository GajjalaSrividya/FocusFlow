package com.focusflow.focusflow.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("tasks")
public class Task {

    @PrimaryKey
    private TaskPrimaryKey key;
    @Column("completed")
    private boolean completed;
}
