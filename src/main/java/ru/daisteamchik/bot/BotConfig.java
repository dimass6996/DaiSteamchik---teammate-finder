package ru.daisteamchik.bot;

// Импортируем библиотеку java-dotenv.
// Она умеет читать файл .env из корня проекта и отдавать значения по ключам.
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.DefaultBotOptions;

public class BotConfig {
    private final String botToken;
    private final String botUsername;
    private final DefaultBotOptions botOptions;

    public BotConfig() {
        // .load() ищет файл .env в рабочей директории проекта и читает его.
        // Системные переменные окружения имеют приоритет над .env.
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
        this.botOptions = createBotOptions(dotenv.get("TELEGRAM_PROXY_HOST"), dotenv.get("TELEGRAM_PROXY_PORT"));
    }

    public String getBotToken() { return botToken; }
    public String getBotUsername() { return botUsername; }
    public DefaultBotOptions getBotOptions() { return botOptions; }

    static DefaultBotOptions createBotOptions(String proxyHost, String proxyPort) {
        DefaultBotOptions options = new DefaultBotOptions();
        String host = proxyHost == null ? "" : proxyHost.trim();
        String port = proxyPort == null ? "" : proxyPort.trim();
        if (host.isEmpty() && port.isEmpty()) {
            return options;
        }
        if (host.isEmpty() || port.isEmpty()) {
            throw new IllegalArgumentException("Для HTTP-прокси укажи TELEGRAM_PROXY_HOST и TELEGRAM_PROXY_PORT в .env.");
        }
        int portNumber;
        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("TELEGRAM_PROXY_PORT должен быть числом от 1 до 65535.");
        }
        if (portNumber < 1 || portNumber > 65535) {
            throw new IllegalArgumentException("TELEGRAM_PROXY_PORT должен быть числом от 1 до 65535.");
        }
        options.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        options.setProxyHost(host);
        options.setProxyPort(portNumber);
        return options;
    }
}
