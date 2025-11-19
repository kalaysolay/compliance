package com.example.compliance.config;

import com.example.compliance.model.User;
import com.example.compliance.model.UserRole;
import com.example.compliance.repo.UserRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Инициализирует базовые данные при запуске.
 */
@Component
public class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(final UserRepository userRepository,
                           final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Создает и обновляет учетные записи по умолчанию.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        createUserIfMissing("user", "Demo User", UserRole.USER, "userpass");
        createUserIfMissing("officer", "Compliance Officer", UserRole.OFFICER, "officerpass");
        userRepository.findAll().forEach(this::encodePasswordIfNeeded);
    }

    private void createUserIfMissing(final String username,
                                     final String fullName,
                                     final UserRole role,
                                     final String rawPassword) {
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            LOGGER.debug("Пользователь {} уже существует", username);
        }, () -> {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername(username);
            user.setFullName(fullName);
            user.setRole(role);
            user.setEnabled(true);
            user.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            LOGGER.info("Создан пользователь {}", username);
        });
    }

    private void encodePasswordIfNeeded(final User user) {
        String password = user.getPasswordHash();
        if (password == null || password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) {
            return;
        }
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
        LOGGER.info("Обновлен пароль пользователя {} до BCrypt", user.getUsername());
    }
}
