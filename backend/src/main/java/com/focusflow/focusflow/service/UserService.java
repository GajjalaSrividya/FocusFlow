package com.focusflow.focusflow.service;

import com.focusflow.focusflow.model.User;
import com.focusflow.focusflow.model.UserLookup;
import com.focusflow.focusflow.repository.UserLookupRepository;
import com.focusflow.focusflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLookupRepository userLookupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String name, String password, String email) {
        UUID userId = UUID.randomUUID();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userId, name, encodedPassword, email);  
        UserLookup lookup = new UserLookup(username, userId.toString());

        userRepository.save(user);
        userLookupRepository.save(lookup);
    }
}
