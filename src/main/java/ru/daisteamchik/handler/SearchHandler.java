package ru.daisteamchik.handler;

import ru.daisteamchik.model.User;
import ru.daisteamchik.repository.UserRepository;
import ru.daisteamchik.service.BotState;
import ru.daisteamchik.service.MessageSender;
import ru.daisteamchik.service.StateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHandler {
    private final MessageSender messageSender;
    private final StateManager stateManager;
    private final UserRepository userRepository = new UserRepository();
    private final Map<Long, List<User>> sessions = new HashMap<>();
    private final Map<Long, Integer> positions = new HashMap<>();
    private final Map<Long, Long> selectedUsers = new HashMap<>();

    public SearchHandler(StateManager stateManager, MessageSender messageSender) {
        this.stateManager = stateManager;
        this.messageSender = messageSender;
    }

    public boolean isSearching(long userId) {
        return sessions.containsKey(userId);
    }

    public String start(long userId) {
        if (isSearching(userId)) {
            return "Ты уже ищешь тиммейта. Ответь 1, чтобы написать, 2 — пропустить, 0 — закончить.";
        }

        List<User> users = userRepository.getUsersFromBd(7).stream()
                .filter(user -> user.getUserId() != userId)
                .toList();
        if (users.isEmpty()) {
            return "Пока нет других анкет для поиска.";
        }

        sessions.put(userId, users);
        positions.put(userId, 0);
        selectedUsers.remove(userId);
        stateManager.setState(userId, BotState.SEARCH_WAITING_MESSAGE);
        return showCurrent(userId);
    }

    public String handle(long userId, String text) {
        List<User> users = sessions.get(userId);
        if (users == null) {
            return "Сначала введи /search.";
        }

        Long recipientId = selectedUsers.get(userId);
        if (recipientId != null) {
            boolean sent = messageSender.send(userId, recipientId, text);
            if (sent) {
                finish(userId);
                return "Сообщение отправлено вместе с твоей анкетой.";
            }
            selectedUsers.remove(userId);
            return "Не удалось отправить сообщение. Возможно, получатель ещё не начал чат с ботом. " +
                    "Ты остался на этой анкете: 1 — попробовать снова, 2 — следующий профиль, 0 — закончить.";
        }

        switch (text.trim()) {
            case "1":
                selectedUsers.put(userId, users.get(positions.get(userId)).getUserId());
                return "Напиши сообщение — я отправлю его вместе с твоей анкетой.";
            case "2":
                int next = positions.get(userId) + 1;
                if (next >= users.size()) {
                    finish(userId);
                    return "Анкеты закончились. Поиск завершён.";
                }
                positions.put(userId, next);
                return showCurrent(userId);
            case "0":
                finish(userId);
                return "Поиск завершён.";
            default:
                return "Ответь 1, чтобы написать, 2 — пропустить, 0 — закончить.";
        }
    }

    private String showCurrent(long userId) {
        int position = positions.get(userId);
        User user = sessions.get(userId).get(position);
        boolean sentAsPicture = messageSender.sendProfile(userId, user);
        return "Анкета " + (position + 1) + " из " + sessions.get(userId).size() +
                (sentAsPicture ? "" : "\n" + user) +
                "\n\n1 — написать, 2 — следующий профиль, 0 — закончить.";
    }

    private void finish(long userId) {
        sessions.remove(userId);
        positions.remove(userId);
        selectedUsers.remove(userId);
        stateManager.setState(userId, BotState.NONE);
    }

    public String cancel(long userId) {
        finish(userId);
        return "Поиск завершён.";
    }

    public String back(long userId) {
        return cancel(userId);
    }
}
