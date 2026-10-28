package ru.daisteamchik.registration;

import ru.daisteamchik.model.User;
import ru.daisteamchik.service.BotState;
import ru.daisteamchik.service.StateManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegistrationHandler {

    private final StateManager stateManager;

    // гнезавершённые анкеты пользователей
    private final Map<Long, RegistrationSession> sessions = new HashMap<>();

    // pарегистрированные пользователи , они в user
    private final Map<Long, User> users = new HashMap<>();

    private final RegistryMessageValidator validator = new RegistryMessageValidator();

    public RegistrationHandler(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public boolean isRegistering(long userId) {
        return sessions.containsKey(userId);
    }

    public User getUser(long userId) {
        return users.get(userId);
    }

    public Map<Long, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    // начинаем регистрацию
    public String start(long userId) {

        if (isRegistering(userId)) {
            return "Ты уже проходишь регистрацию!";
        }

        sessions.put(userId, new RegistrationSession());

        stateManager.setState(userId, BotState.REG_WAITING_NAME);

        return "Как тебя зовут?";
    }

    // обрабатывваем ответы
    public String handle(long userId, String text) {

        RegistrationSession session = sessions.get(userId);

        if (session == null) {
            return "Сначала введи /registry";
        }

        BotState state = stateManager.getState(userId);

        text = (text == null) ? "" : text.trim();

        switch (state) {

            case REG_WAITING_NAME: {
                String error = validator.getNameError(text);
                if (error != null) {
                    return error;
                }
                session.setName(text);
                stateManager.setState(userId, BotState.REG_WAITING_STEAM_URL);
                return "Отправь ссылку на свой Steam-профиль:";
            }

            case REG_WAITING_STEAM_URL: {
                String error = validator.getUrlError(text);
                if (error != null) {
                    return error;
                }
                session.setSteamUrl(text);
                stateManager.setState(userId, BotState.REG_WAITING_PRIME_TIME);
                return "В какое время ты обычно играешь?";
            }

            case REG_WAITING_PRIME_TIME: {
                String error = validator.getPrimeTimeError(text);
                if (error != null) {
                    return error;
                }
                session.setPrimeTime(text);
                stateManager.setState(userId, BotState.REG_WAITING_ROLE);
                return "Укажи свою роль (например, Керри, Саппорт, Капитан):";
            }

            case REG_WAITING_ROLE: {
                String error = validator.getRolesError(text);
                if (error != null) {
                    return error;
                }
                session.setRole(text);
                stateManager.setState(userId, BotState.REG_WAITING_GAMES);
                return "В какие игры ты играешь?";
            }

            case REG_WAITING_GAMES: {
                String error = validator.getGamesError(text);
                if (error != null) {
                    return error;
                }
                session.setGames(text);

                User user = User.fromSession(userId, session);
                users.put(userId, user);

                sessions.remove(userId);
                stateManager.setState(userId, BotState.DONE);

                return "Регистрация завершена!\n\n" +
                        "Имя: " + user.getName() + "\n" +
                        "Steam: " + user.getSteamUrl() + "\n" +
                        "Прайм-тайм: " + user.getPrimeTime() + "\n" +
                        "Роль: " + user.getRole() + "\n" +
                        "Игры: " + user.getGames();
            }

            default:
                return "Ошибка регистрации.";
        }
    }

    public String cancel(long userId) {
        sessions.remove(userId);
        stateManager.setState(userId, BotState.NONE);
        return "Регистрация отменена.";
    }

    public String back(long userId) {
        BotState currentState = stateManager.getState(userId);

        switch (currentState) {

            case REG_WAITING_STEAM_URL:
                stateManager.setState(userId, BotState.REG_WAITING_NAME);
                return "Возврат к вводу имени. Как тебя зовут?";

            case REG_WAITING_PRIME_TIME:
                stateManager.setState(userId, BotState.REG_WAITING_STEAM_URL);
                return "Возврат к вводу ссылки. Отправь ссылку на свой Steam-профиль:";

            case REG_WAITING_ROLE:
                stateManager.setState(userId, BotState.REG_WAITING_PRIME_TIME);
                return "Возврат к вводу времени. В какое время ты обычно играешь?";

            case REG_WAITING_GAMES:
                stateManager.setState(userId, BotState.REG_WAITING_ROLE);
                return "Возврат к вводу роли. Укажи свою роль:";

            case REG_WAITING_NAME:
                return "Ты находишься на первом шаге регистрации. Используй /cancel для отмены.";

            default:
                return "Невозможно вернуться назад.";
        }
    }
}