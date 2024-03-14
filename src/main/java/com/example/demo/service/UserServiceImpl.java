package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Try to load user by username {}", username);
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            log.error("Пользователь не найден {}", user);
            throw new UsernameNotFoundException("Пользователь не найден");
        } else {
            UserDetails userDetails = convertEntityToUserDetails(user.get());
            log.info("Пользователь {} успешно загружен", user);
            return userDetails;
        }
    }

    private UserDetails convertEntityToUserDetails(User user) {
        String[] authorities = user.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority().name().toUpperCase())
                .toArray(String[]::new);
        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.builder();
        UserDetails userDetails = userBuilder
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(authorities)
                .passwordEncoder(password -> password)
                .build();
        return userDetails;
    }
}
