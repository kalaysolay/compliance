# Compliance MVP

Минимально жизнеспособная система автоматизации комплаенс-процессов на Spring Boot с серверным рендерингом.

## Запуск

1. Создайте файл `.env` (опционально):
   ```properties
   SPRING_PROFILES_ACTIVE=dev
   DB_URL=jdbc:postgresql://localhost:5432/compliance
   DB_USER=dev
   DB_PASS=dev
   ```
2. Примените миграции Flyway (выполняются автоматически при старте приложения).
3. Запустите приложение командой:
   ```bash
   gradle bootRun
   ```

По умолчанию используется встраиваемая база H2. Профиль `dev` переключает подключение на PostgreSQL.

## Демо-учетные записи

| Логин   | Пароль       | Роль     |
|---------|--------------|----------|
| user    | userpass     | USER     |
| officer | officerpass  | OFFICER  |

Пароли автоматически хешируются BCrypt при первом запуске приложения.

## Тестирование

```bash
gradle test
```

## Структура

- `src/main/java` — конфигурация, контроллеры, сервисы и модели.
- `src/main/resources/templates` — шаблоны Thymeleaf с разметкой AdminLTE.
- `src/main/resources/db/migration` — миграции Flyway.
- `src/test/java` — базовые интеграционные и сервисные тесты.
