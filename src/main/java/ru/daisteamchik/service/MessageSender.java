
package ru.daisteamchik.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.daisteamchik.model.User;
import ru.daisteamchik.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class MessageSender {

    private final TelegramLongPollingBot bot;
    private final UserRepository userRepository = new UserRepository();
    private final Map<Long, Long> pendingReplies = new HashMap<>();
    private final Map<Long, Long> replyDrafts = new HashMap<>();

    public MessageSender(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public boolean send(long senderId,long userId, String text) {
        return deliver(senderId, userId, text, true);
    }

    public boolean isReplying(long userId) {
        return pendingReplies.containsKey(userId) || replyDrafts.containsKey(userId);
    }

    public String handleReply(long userId, String text) {
        Long senderId = replyDrafts.get(userId);
        if (senderId != null) {
            if (deliver(userId, senderId, text, false)) {
                replyDrafts.remove(userId);
                pendingReplies.remove(userId);
                return "Ответ отправлен вместе с твоей анкетой.";
            }
            return "Не удалось отправить ответ. Напиши его ещё раз.";
        }

        if (!"1".equals(text.trim())) {
            return "Чтобы ответить на сообщение, отправь 1.";
        }
        replyDrafts.put(userId, pendingReplies.get(userId));
        return "Напиши ответ одним сообщением.";
    }

    private boolean deliver(long senderId, long userId, String text, boolean allowReply) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText("Сообщение от пользователя:\n" + text + (allowReply ? "\n\n1 — ответить на сообщение." : ""));
        User sender = userRepository.getUserFromBd(senderId);

        try {
            bot.execute(message);
            if (allowReply) {
                pendingReplies.put(userId, senderId);
            }
            if (sender != null && !sendProfile(userId, sender)) {
                SendMessage fallback = new SendMessage();
                fallback.setChatId(userId);
                fallback.setText("Анкета отправителя:\n" + sender);
                bot.execute(fallback);
            }
            return true;
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
            return false;
        }
    }

    public boolean sendProfile(long chatId, User user) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        if (user.getSteamUrl() != null && !user.getSteamUrl().isBlank()) {
            photo.setCaption("Steam: " + user.getSteamUrl());
        }
        try {
            photo.setPhoto(user.generateProfilePicture(bot));
            bot.execute(photo);
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка отправки анкеты-картинки: " + e.getMessage());
            return false;
        }
    }
}
