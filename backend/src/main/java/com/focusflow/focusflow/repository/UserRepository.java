package com.focusflow.focusflow.repository;

import com.focusflow.focusflow.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CassandraRepository<User, UUID> {
    Optional<User> findByName(String name); 
}
