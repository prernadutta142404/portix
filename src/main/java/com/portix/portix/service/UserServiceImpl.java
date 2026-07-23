package com.portix.portix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portix.portix.entity.User;
import com.portix.portix.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {

        // Check if email is already registered
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Hash password before saving
        String hashedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {

        return userRepository.findByEmail(email)
                .filter(user -> {

                    String storedPassword = user.getPassword();

                    // New accounts: BCrypt password
                    if (storedPassword != null &&
                        storedPassword.startsWith("$2")) {

                        return passwordEncoder.matches(
                                password,
                                storedPassword
                        );
                    }

                    // Temporary support for old test accounts
                    return storedPassword != null &&
                           storedPassword.equals(password);
                })
                .orElse(null);
    }
    
    @Override
    public User findById(Long id) {

        return userRepository.findById(id)
                .orElse(null);
    }
    
}