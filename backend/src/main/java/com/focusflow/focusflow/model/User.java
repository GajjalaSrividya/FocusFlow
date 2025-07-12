package com.focusflow.focusflow.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @PrimaryKey("user_id") 
    private UUID userId;

    @Column("name")
    private String name;

    @Column("password")
    private String password;

    @Column("email")  
    private String email;
}
