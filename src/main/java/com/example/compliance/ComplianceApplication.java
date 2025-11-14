package com.example.compliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс приложения комплаенс-портала.
 */
@SpringBootApplication
public class ComplianceApplication {

    /**
     * Точка входа Spring Boot.
     *
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {
        SpringApplication.run(ComplianceApplication.class, args);
    }
}
