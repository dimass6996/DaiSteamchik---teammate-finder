package ru.daisteamchik.command;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.daisteamchik.service.StateManager;
import ru.daisteamchik.registration.*;

public class RegistryHandlerTest {
    private StateManager stateManager = new StateManager();
    private RegistrationHandler registrationHandler = new RegistrationHandler(stateManager);
    private int userId = 1234;

    @Test
    void testRegistrationHandler(){
        assertTrue(registrationHandler.start(userId) == "Как тебя зовут?");

        // проверка ввода имени
        assertTrue(registrationHandler.handle(userId,"Антошка") == "Отправь ссылку на свой Steam-профиль:");

        // проверка ввода ссылки на steam
        // ждет добавления валидации assertTrue(registrationHandler.handle(userId,"matmech.ru") != "В какое время ты обычно играешь?");
        assertTrue(registrationHandler.handle(userId,"https://steamcommunity.com/profiles/76561198174543473") == "В какое время ты обычно играешь?");

        // проверка ввода PRIME TIME
        // ждет добавления валидации assertTrue(registrationHandler.handle(userId,"67:00 - 253:00") != "Какую роль предпочитаешь в команде?");
        assertTrue(registrationHandler.handle(userId,"22:00 - 23:00") == "Какую роль предпочитаешь в команде?");

        // проверка ввода списка ролей
        assertTrue(registrationHandler.handle(userId,"керри, саппорт") == "В какие игры ты играешь?");

        // проверка ввода списка игр

        assertTrue(registrationHandler.handle(userId,"dota 2, cs 2") == "Регистрация успешно завершена!");




    }
}
