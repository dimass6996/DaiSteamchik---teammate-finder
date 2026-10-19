package ru.daisteamchik.registration;

import ru.daisteamchik.service.BotEvent;
import ru.daisteamchik.service.BotState;
import ru.daisteamchik.service.StateManager;

import java.util.HashMap;
import java.util.Map;

public class RegistrationHandler {

    private final StateManager stateManager;

    // Незавершённые анкеты пользователей
    private final Map<Long, RegistrationSession> sessions = new HashMap<>();

    public RegistrationHandler(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    private RegistryMessageValidator validator = new RegistryMessageValidator();

    public boolean isRegistering(long userId) {
        return sessions.containsKey(userId);
    }
    // Начало регистрации
    public String start(long userId) {

        if (isRegistering(userId)) {
            return "Ты уже проходишь регистрацию!";
        }

        sessions.put(userId, new RegistrationSession());

        stateManager.setState(userId, BotState.REG_WAITING_NAME);

        return "Как тебя зовут?";
    }

    // Проверка активной регистрации

    // Обработка ответов
    public String handle(long userId, String text) {

        RegistrationSession session = sessions.get(userId);

        if (session == null) {
            return "Сначала введи /registry";
        }

        BotState state = stateManager.getState(userId);

        text = text.trim();

        switch (state) {

            case REG_WAITING_NAME:

                if (text.isEmpty()) {
                    return "Имя не может быть пустым!";
                }

                // валидация имени
                if (validator.validateName(text)) {
                    session.setName(text);

                    stateManager.setState(
                            userId, BotState.REG_WAITING_STEAM_URL
                    );

                    return "Отправь ссылку на свой Steam-профиль:";
                }else{
                    return "проверь корректность ввода имени";
                }



            case REG_WAITING_STEAM_URL:

                if (text.isEmpty()) {
                    return "Ссылка не может быть пустой!";
                }
                if (validator.validateUrl(text)) {
                    session.setSteamUrl(text);

                    stateManager.setState(
                            userId, BotState.REG_WAITING_PRIME_TIME
                    );

                    return "В какое время ты обычно играешь?";
                }else{
                    return "проверь корректность ссылки";
                }


            case REG_WAITING_PRIME_TIME:

                if (text.isEmpty()) {
                    return "Укажи время, когда обычно играешь!";
                }

                session.setPrimeTime(text);

                stateManager.setState(
                        userId, BotState.REG_WAITING_ROLE
                );

                return "Какую роль предпочитаешь в команде?";


            case REG_WAITING_ROLE:

                if (text.isEmpty()) {
                    return "Роль не может быть пустой!";
                }
                if (validator.validateRoles(text)) {

                    session.setRole(text);

                    stateManager.setState(
                            userId, BotState.REG_WAITING_GAMES
                    );

                    return "В какие игры ты играешь?";
                }else{
                    return "проверь корректность введенных ролей";
                }


            case REG_WAITING_GAMES:

                if (text.isEmpty()) {
                    return "Укажи хотя бы одну игру!";
                }
                if (validator.validateGames(text)) {
                    session.setGames(text);

                    // Позже здесь будет сохранение в базу данных
                    System.out.println("Новая анкета:");
                    System.out.println("User ID: " + userId);
                    System.out.println("Имя: " + session.getName());
                    System.out.println("Steam: " + session.getSteamUrl());
                    System.out.println("Прайм-тайм: " + session.getPrimeTime());
                    System.out.println("Роль: " + session.getRole());
                    System.out.println("Игры: " + session.getGames());

                    stateManager.setState(userId, BotState.DONE);
                    sessions.remove(userId);

                    return "Регистрация успешно завершена!";
                }else{
                    return "проверь корректность введенных игр";
                }

            default:
                return "Неизвестный этап регистрации!";
        }
    }

    public String back(long userId) {
        stateManager.next(userId, BotEvent.CMD_BACK);

        BotState state = stateManager.getState(userId);

        switch (state) {
            case REG_WAITING_NAME:
                return "Как тебя зовут?";
            case REG_WAITING_STEAM_URL:
                return "Отправь ссылку на свой Steam-профиль:";
            case REG_WAITING_PRIME_TIME:
                return "В какое время ты обычно играешь?";
            case REG_WAITING_ROLE:
                return "Какую роль предпочитаешь в команде?";
            case NONE:
                sessions.remove(userId);
                return "Регистрация отменена!";
            default:
                return "Ошибка возврата!";
        }
    }
    // Отмена регистрации
    public void cancel(long userId) {

        sessions.remove(userId);

        stateManager.setState(userId, BotState.NONE);
    }
}