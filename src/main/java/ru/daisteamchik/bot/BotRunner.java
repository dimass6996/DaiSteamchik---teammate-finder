package ru.daisteamchik.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.daisteamchik.command.*;
import ru.daisteamchik.handler.RegistrationHandler;
import ru.daisteamchik.handler.SearchHandler;
import ru.daisteamchik.service.MessageSender;
import ru.daisteamchik.service.StateManager;
import java.util.StringJoiner;

public class BotRunner {
    public static void start() {
        BotConfig config = new BotConfig();
        Bot bot = null;
        try {
            StateManager stateManager = new StateManager();
            RegistrationHandler registrationHandler = new RegistrationHandler(stateManager);
            CommandRegistry registry = new CommandRegistry();
            registry.register(new HelpCommand(registry));
            registry.register(new AboutCommand());
            registry.register(new AuthorCommand());
            registry.register(new StartCommand());
            registry.register(new RegistryCommand(registrationHandler));
            registry.register(new BackCommand(registrationHandler));
            registry.register(new CancelCommand(registrationHandler));
            registry.register(new ProfileCommand());
            bot = new Bot(config.getBotOptions(), config.getBotToken(), config.getBotUsername(), registry,registrationHandler);
            MessageSender sender = new MessageSender(bot);
            SearchHandler searchHandler = new SearchHandler(stateManager, sender);
            bot.setSearchHandler(searchHandler);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            registry.register(new SearchCommand(searchHandler));
            System.out.println("Бот запущен: " + config.getBotUsername());
        } catch (TelegramApiException e) {
            System.err.println(describeError(e, config.getBotToken()));
            if (bot != null) {
                bot.onClosing();
            }
        }
    }

    static String describeError(Throwable error, String botToken) {
        StringJoiner details = new StringJoiner(System.lineSeparator() + "  Причина: ");
        for (Throwable cause = error; cause != null; cause = cause.getCause()) {
            String message = String.valueOf(cause.getMessage()).replace(botToken, "[TOKEN]");
            details.add(cause.getClass().getSimpleName() + ": " + message);
        }
        return "Ошибка запуска: " + details;
    }
}
