package ru.daisteamchik.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.daisteamchik.command.*;

public class BotRunner {
    public static void start() {
        try {
            BotConfig config = new BotConfig();
            CommandRegistry registry = new CommandRegistry();
            registry.register(new HelpCommand(registry));
            registry.register(new AboutCommand());
            registry.register(new AuthorCommand());
            registry.register(new StartCommand());

            Bot bot = new Bot(config.getBotToken(), config.getBotUsername(), registry);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Бот запущен: " + config.getBotUsername());
        } catch (TelegramApiException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}