package ru.daisteamchik.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.daisteamchik.command.Command;
import ru.daisteamchik.command.CommandRegistry;
import ru.daisteamchik.command.CommandResponse;
import ru.daisteamchik.handler.RegistrationHandler;

import java.util.Arrays;

public class Bot extends TelegramLongPollingBot {

    private final String botUsername;
    private final CommandRegistry commandRegistry;
    private final RegistrationHandler registrationHandler;

    public Bot(
            String botToken,
            String botUsername,
            CommandRegistry commandRegistry,
            RegistrationHandler registrationHandler
    ) {
        super(botToken);

        this.botUsername = botUsername;
        this.commandRegistry = commandRegistry;
        this.registrationHandler = registrationHandler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText().trim();

        long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();

        CommandResponse response = null;

        // Обработка всех команд
        if (text.startsWith("/")) {

            String[] parts = text.split("\\s+");

            String commandName = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            Command command = commandRegistry.getCommand(commandName);

            if (command != null) {
                response = command.execute(userId, args);
            } else {
                response = new CommandResponse("Неизвестная команда. Используй /help");
            }

        }
        // обработка ответов во время регистрации
        else if (registrationHandler.isRegistering(userId)) {
            response = new CommandResponse(registrationHandler.handle(userId, text));
        } else {
            response = new CommandResponse("Не понял тебя, введи /help чтобы узнать доступные команды");
        }

        // Отправка ответа
        if (response != null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);

            message.setText(response.getMessage());
            message.setParseMode("HTML");

            if (response.getKeyboard() != null) {
                message.setReplyMarkup(response.getKeyboard());
            }

            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}