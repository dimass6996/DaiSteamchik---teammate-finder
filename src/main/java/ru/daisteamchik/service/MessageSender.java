
package ru.daisteamchik.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSender {

    private final TelegramLongPollingBot bot;

    public MessageSender(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public boolean send(long userId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(text);

        try {
            bot.execute(message);
            return true;
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
            return false;
        }
    }
}
