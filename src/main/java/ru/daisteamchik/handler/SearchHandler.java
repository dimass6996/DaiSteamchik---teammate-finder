package ru.daisteamchik.handler;

import ru.daisteamchik.service.BotState;
import ru.daisteamchik.service.StateManager;
import ru.daisteamchik.model.User;
import ru.daisteamchik.repository.UserRepository;
import ru.daisteamchik.service.MessageSender;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SearchHandler {

    private final MessageSender messageSender;
    private final StateManager stateManager;

    private final UserRepository userRep = new UserRepository();
    private final List<Long> sessions = new ArrayList<>();

    private final Map<Long, Integer> pages = new HashMap<>();


    public SearchHandler(StateManager stateManager, MessageSender messageSender) {
        this.stateManager = stateManager;
        this.messageSender = messageSender;
    }

    private List<User> askBD(int limit) {

        return userRep.getUsersFromBd(limit);
    }

    public boolean isSearching(long chatId){
        return sessions.contains(chatId);
    }


    public String start(long chatId){
        if (sessions.contains(chatId)){
            return "Ты уже в поиске тиммейтов:";
        }

        sessions.add(chatId);
        stateManager.setState(chatId, BotState.SEARCH_WAITING_MESSAGE);
        List<User> users = askBD(7);

        StringBuilder sb = new StringBuilder();
        sb.append("Выбери пользователя которому хочешь отправить сообщение\n");
        for (User s : users){
            sb.append(s.toShortString());
        }
        sb.append("Введи сообщение для пользователя в формате:" +
                "{id пользователя} : {Сообщение для пользователя + твой телеграмм для связи}");
        return sb.toString();
    }

    public String handle(long chatId,String text){

        if (!sessions.contains(chatId)){
            return "ты еще не начал поиск, введи /search";
        }

        BotState state = stateManager.getState(chatId);

        switch (state){
            case SEARCH_WAITING_MESSAGE:
                String[] parts = text.split(":", 2);
                long id = Long.parseLong(parts[0]);
                String message = parts[1];
                messageSender.send(id,message);
                return "сообщение отправлено";
        default:
            return "Ошибка отправки";

        }

    }

    public String cancel(long userId) {
        sessions.remove(userId);
        stateManager.setState(userId, BotState.NONE);
        return "Поиск отменен";
    }

    public String back(long userId) {
        sessions.remove(userId);
        stateManager.setState(userId, BotState.NONE);
        return "Поиск отменен";
    }
}
