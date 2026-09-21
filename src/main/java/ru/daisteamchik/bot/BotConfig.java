package ru.daisteamchik.bot;

// Импортируем библиотеку java-dotenv.
// Она умеет читать файл .env из корня проекта и отдавать значения по ключам.
import io.github.cdimascio.dotenv.Dotenv;

public class BotConfig {
    private final String botToken;
    private final String botUsername;

    public BotConfig() {
        // .load() ищет файл .env в рабочей директории проекта и читает его.
        // Значения из .env "важнее" системных переменных окружения.
        Dotenv dotenv = Dotenv.configure().load();

        // Берём токен и имя бота из файла .env.
        this.botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
        this.botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");

        // Если в .env нет токена или он пустой — сразу сообщаем об ошибке,
        // чтобы бот не запустился с пустыми настройками.
        if (botToken == null || botToken.isEmpty()) {
            throw new RuntimeException("TELEGRAM_BOT_TOKEN не установлен! Проверь файл .env в корне проекта.");
        }
        if (botUsername == null || botUsername.isEmpty()) {
            throw new RuntimeException("TELEGRAM_BOT_USERNAME не установлен! Проверь файл .env в корне проекта.");
        }
    }

    public String getBotToken() { return botToken; }
    public String getBotUsername() { return botUsername; }
}