package ru.daisteamchik.bot;

public class BotConfig {
    private final String botToken;
    private final String botUsername;

    public BotConfig() {
        this.botToken = System.getenv("TELEGRAM_BOT_TOKEN");
        this.botUsername = System.getenv("TELEGRAM_BOT_USERNAME");

        if (botToken == null || botToken.isEmpty()) {
            throw new RuntimeException("TELEGRAM_BOT_TOKEN не установлен!");
        }
        if (botUsername == null || botUsername.isEmpty()) {
            throw new RuntimeException("TELEGRAM_BOT_USERNAME не установлен!");
        }
    }

    public String getBotToken() { return botToken; }
    public String getBotUsername() { return botUsername; }
}