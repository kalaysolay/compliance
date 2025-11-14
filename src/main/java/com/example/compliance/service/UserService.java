package com.example.compliance.service;

import com.example.compliance.model.User;
import com.example.compliance.repo.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис работы с пользователями.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получает пользователя по имени.
     *
     * @param username логин
     * @return найденный пользователь
     */
    @Transactional(readOnly = true)
    public User loadByUsername(final String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор
     * @return найденный пользователь
     */
    @Transactional(readOnly = true)
    public User loadById(final UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
