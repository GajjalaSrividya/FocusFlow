package com.focusflow.focusflow.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user_lookup")
public class UserLookup {

    @PrimaryKey
    private String username;

    @Column("user_id")
    private String userId;
}
