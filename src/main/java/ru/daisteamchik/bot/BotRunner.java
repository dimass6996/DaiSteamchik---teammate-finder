package ru.daisteamchik.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.daisteamchik.command.*;
import ru.daisteamchik.registration.RegistrationHandler;
import ru.daisteamchik.service.StateManager;
import ru.daisteamchik.registration.RegistrationHandler;

public class BotRunner {
    public static void start() {
        try {
            BotConfig config = new BotConfig();
            StateManager stateManager = new StateManager();
            RegistrationHandler registrationHandler = new RegistrationHandler(stateManager);
            CommandRegistry registry = new CommandRegistry();
            registry.register(new HelpCommand(registry));
            registry.register(new AboutCommand());
            registry.register(new AuthorCommand());
            registry.register(new StartCommand());

            Bot bot = new Bot(config.getBotToken(), config.getBotUsername(), registry,registrationHandler);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Бот запущен: " + config.getBotUsername());
        } catch (TelegramApiException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}