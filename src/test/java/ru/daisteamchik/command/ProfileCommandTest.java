package ru.daisteamchik.command;

import org.junit.jupiter.api.Test;
import ru.daisteamchik.service.StateManager;
import ru.daisteamchik.registration.RegistrationHandler;
import static org.junit.jupiter.api.Assertions.*;

class ProfileCommandTest {

    @Test
    void checkCommandInfo() {
        StateManager stateManager = new StateManager();
        RegistrationHandler handler = new RegistrationHandler(stateManager);
        ProfileCommand command = new ProfileCommand(handler);

        assertEquals("/profile", command.getName());
        assertNotNull(command.getDescription());
    }

    @Test
    void checkExecuteWithoutProfile() {
        StateManager stateManager = new StateManager();
        RegistrationHandler handler = new RegistrationHandler(stateManager);
        ProfileCommand command = new ProfileCommand(handler);

        // тут рандомный айди чтобы проверить бота
        CommandResponse response = command.execute(999999L, new String[]{});

        assertNotNull(response, "Ответ не должен быть null");
        assertEquals("У тебя еще нет анкеты. Напиши /reg, чтобы зарегистрироваться.", response.getMessage());
    }
}