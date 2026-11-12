package ru.daisteamchik.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.daisteamchik.command.Command;
import ru.daisteamchik.command.CommandRegistry;
import ru.daisteamchik.command.CommandResponse;
import ru.daisteamchik.handler.RegistrationHandler;
import ru.daisteamchik.handler.SearchHandler;
import ru.daisteamchik.model.User;
import ru.daisteamchik.service.MessageSender;
import ru.daisteamchik.command.ProfileCommand;

import java.util.Arrays;

public class Bot extends TelegramLongPollingBot {

    private final String botUsername;
    private final CommandRegistry commandRegistry;
    private final RegistrationHandler registrationHandler;
    private final MessageSender profileSender;
    public Bot(
            DefaultBotOptions options,
            String botToken,
            String botUsername,
            CommandRegistry commandRegistry,
            RegistrationHandler registrationHandler
    ) {
        super(options, botToken);

        this.botUsername = botUsername;
        this.commandRegistry = commandRegistry;
        this.registrationHandler = registrationHandler;
        this.profileSender = new MessageSender(this);
    }

    private SearchHandler searchHandler;

    public void setSearchHandler(SearchHandler searchHandler) {
        this.searchHandler = searchHandler;
    }

    public MessageSender getMessageSender() {
        return profileSender;
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

        String response = null;
        User profilePicture = null;

        // Обработка всех команд
        if (text.startsWith("/")) {

            String[] parts = text.split("\\s+");

            String commandName = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            Command command = commandRegistry.getCommand(commandName);

            if (command != null) {
                CommandResponse answer = command.execute(userId, args);
                response = answer.getMessage();
                if (command instanceof ProfileCommand) {
                    profilePicture = ((ProfileCommand) command).getUser(userId);
                    if (profilePicture != null) {
                        response = null;
                    }
                }
            } else {
                response = "Неизвестная команда. Используй /help";
            }

        }

        // Обработка ответов во время регистрации
        else if (registrationHandler.isRegistering(userId)) {

            response = registrationHandler.handle(userId, text);
            if (!registrationHandler.isRegistering(userId)) {
                profilePicture = registrationHandler.getUser(userId);
            }

        } else if (profileSender.isReplying(userId)) {

            response = profileSender.handleReply(userId, text);

        } else if (searchHandler != null && searchHandler.isSearching(userId)) {

            response = searchHandler.handle(userId, text);

        } else {
            response = "Не понял тебя, введи /help чтобы узнать доступные комманды";
        }

        // Отправка ответа
        if (response != null) {

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(response);

            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (profilePicture != null && !profileSender.sendProfile(chatId, profilePicture)) {
            SendMessage fallback = new SendMessage();
            fallback.setChatId(chatId);
            fallback.setText(profilePicture.toString());
            try {
                execute(fallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
