package com.example.compliance.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Thymeleaf: подключает layout-dialect для поддержки layout:decorate.
 */
@Configuration
public class ThymeleafConfig {

    /**
     * Регистрируем LayoutDialect, иначе теги layout:* приводят к ошибкам на страницах.
     *
     * @return экземпляр диалекта
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
