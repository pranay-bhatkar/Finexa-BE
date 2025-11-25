package com.expense_tracker.service;

import com.expense_tracker.dto.user.UserResponseDTO;
import com.expense_tracker.exception.AccessDeniedException;
import com.expense_tracker.exception.UserAlreadyExistException;
import com.expense_tracker.exception.UserNotFoundException;
import com.expense_tracker.model.Role;
import com.expense_tracker.model.User;
import com.expense_tracker.repository.UserRepository;
import com.expense_tracker.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;


    @Transactional
    public User saveUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists with email : " + user.getEmail());
        }

        // encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    public Page<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir, boolean all) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        List<User> usersList;

        if (all) {
            // Return all users as a single page
            usersList = userRepository.findAll(sort);
        } else {
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> usersPage = userRepository.findAll(pageable);

            if (usersPage.isEmpty()) {
                throw new UserNotFoundException("No users found for page: " + page);
            }

            usersList = usersPage.getContent();
        }

        // Map entities -> DTOs
        List<UserResponseDTO> dtoList = usersList.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))
                .toList();

        return new PageImpl<>(dtoList);
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("User not found with ID : " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email));
    }

    public User deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with : " + id));

        userRepository.deleteById(id);

        return user;
    }

    // update all user details { put }
    public User updateUser(Long id, User updateUser) {
        User existingUser = userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("User not found with ID : " + id));

        String oldEmail = existingUser.getEmail(); // store previous email


        // check for duplicates email (except current user)
        if (userRepository.findByEmail(updateUser.getEmail()).filter(
                user -> !user.getId().equals(id)).isPresent()) {
            throw new UserAlreadyExistException("Email already in use : " + updateUser.getEmail());
        }

        existingUser.setName(updateUser.getName());
        existingUser.setEmail(updateUser.getEmail());

//      existingUser.setPassword(updateUser.getPassword());

        // if password provided in updateUser, encode it; otherwise keep existing
        if (updateUser.getPassword() != null && !updateUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);

        notificationService.sendNotification(
                savedUser,
                "👤 Profile Updated",
                "Your profile information was updated."
        );

        //  Send Email Updated Notification ONLY IF email changed
        if (!oldEmail.equals(updateUser.getEmail())) {
            notificationService.sendNotification(
                    existingUser,
                    "📧 Email Updated",
                    "Your email address was changed from: " + oldEmail +
                            " → " + updateUser.getEmail()
            );
        }

        return savedUser;
    }


    // update partial details { patch }
    public User patchUser(Long id, Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID : " + id));

        if (updates.containsKey("role")) {
            throw new IllegalArgumentException("Role cannot be modified through this endpoint");
        }


        // apply only the provided updates
        updates.forEach((field, value) -> {
            switch (field) {
                case "name" -> existingUser.setName((String) value);

                case "email" -> {

                    String oldEmail = existingUser.getEmail();
                    String newEmail = (String) value;

                    if (userRepository.findByEmail((String) value).filter(user -> !user.getId().equals(id)).isPresent()) {
                        throw new UserAlreadyExistException("Email already in use : " + value);
                    }
                    existingUser.setEmail(newEmail);

                    if (!oldEmail.equals(newEmail)) {
                        notificationService.sendNotification(
                                existingUser,
                                "📧 Email Updated",
                                "Your email was changed from: " + oldEmail + " → " + newEmail
                        );
                    }
                }

                case "password" -> existingUser.setPassword(passwordEncoder.encode((String) value));

                default -> throw new IllegalArgumentException("Invalid field : " + field);
            }
        });

        return userRepository.save(existingUser);
    }

    public void deleteAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found to delete");
        }
        userRepository.deleteAll();
    }

    public User changeUserRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID : " + id));

        user.setRole(newRole);
        return userRepository.save(user);
    }

    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String email = authentication.getName(); // extract from jwt / or from login auth

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }


    // Total number of users
    public long countAllUsers() {
        return userRepository.count();
    }

    // Active users (example: users with at least one transaction, or based on your active logic)
    public long countActiveUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.isAccountLocked()) // assuming you have 'active' flag, otherwise define your criteria
                .count();
    }
}