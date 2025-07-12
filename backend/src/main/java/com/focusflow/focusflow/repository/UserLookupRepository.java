package com.focusflow.focusflow.repository;

import com.focusflow.focusflow.model.UserLookup;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLookupRepository extends CassandraRepository<UserLookup, String> {
}
