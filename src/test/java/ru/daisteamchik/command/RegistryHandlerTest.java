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
        assertEquals("Как тебя зовут?", registrationHandler.start(userId));

        assertEquals("Отправь ссылку на свой Steam-профиль:", registrationHandler.handle(userId,"Димасик"));

        assertEquals("В какое время ты обычно играешь?", registrationHandler.handle(userId,"https://steamcommunity.com/profiles/76561198174543473"));

        assertEquals("Укажи свою роль (например, Керри, Саппорт, Капитан):", registrationHandler.handle(userId,"22:00 - 23:00"));

        assertEquals("В какие игры ты играешь?", registrationHandler.handle(userId,"керри, саппорт"));

        String expectedFinalMessage = "Регистрация завершена!\n\n" +
                "Имя: Димасик\n" +
                "Steam: https://steamcommunity.com/profiles/76561198174543473\n" +
                "Прайм-тайм: 22:00 - 23:00\n" +
                "Роль: керри, саппорт\n" +
                "Игры: dota 2, cs 2";

        assertEquals(expectedFinalMessage, registrationHandler.handle(userId,"dota 2, cs 2"));
    }
}