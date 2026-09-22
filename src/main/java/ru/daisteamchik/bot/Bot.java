package ru.daisteamchik.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.daisteamchik.command.Command;
import ru.daisteamchik.command.CommandRegistry;

import java.util.Arrays;

public class Bot extends TelegramLongPollingBot {
    private final String botUsername;
    private final CommandRegistry commandRegistry;

    public Bot(String botToken, String botUsername, CommandRegistry commandRegistry) {
        super(botToken);
        this.botUsername = botUsername;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public String getBotUsername() { return botUsername; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            if (text.startsWith("/")) {

                String[] parts = text.split(" "); // разделяю сообщение
                String commandName = parts[0]; // беру саму команду
                String[] args = Arrays.copyOfRange(parts,1,parts.length); // беру аргументы

                Command command = commandRegistry.getCommand(commandName);
                if (command != null) {
                    String answer = command.execute(args);

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText(answer);

                    try {
                        execute(message);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}