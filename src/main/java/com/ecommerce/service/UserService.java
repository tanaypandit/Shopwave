	package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Comes from PasswordConfig — no circular reference
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use: " + user.getEmail());
        }

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ✅ IMPORTANT: Do NOT override role blindly
        if (user.getRole() == null) {
            user.setRole(User.Role.CUSTOMER);
        }

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    @Transactional
    public User updateProfile(User updatedUser) {
        User existing = findById(updatedUser.getId());
        existing.setFullName(updatedUser.getFullName());
        existing.setPhone(updatedUser.getPhone());
        return userRepository.save(existing);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }
    
 // Add this method inside UserService
    public List<User> getAllDeliveryPartners() {
        return userRepository.findByRole(User.Role.DELIVERY);
    }
}