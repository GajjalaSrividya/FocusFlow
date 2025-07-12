package com.focusflow.focusflow.security;

import com.focusflow.focusflow.model.User;
import com.focusflow.focusflow.model.UserLookup;
import com.focusflow.focusflow.repository.UserLookupRepository;
import com.focusflow.focusflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLookupRepository userLookupRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLookup lookup = userLookupRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UUID userId = UUID.fromString(lookup.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("Loading user by username: " + username);
        return new UserDetailsImpl(username, user.getPassword());
    }
}
