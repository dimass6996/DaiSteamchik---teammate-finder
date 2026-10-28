package ru.daisteamchik.command;

import org.junit.jupiter.api.Test;
import ru.daisteamchik.service.StateManager;
import static org.junit.jupiter.api.Assertions.*;

class RegCommandTest {

    @Test
    void checkCommandInfo() {
        StateManager stateManager = new StateManager();
        RegCommand command = new RegCommand(stateManager);

        assertEquals("/reg", command.getName());
        assertNotNull(command.getDescription());
    }

    @Test
    void checkExecuteReturnsResponse() {
        StateManager stateManager = new StateManager();
        RegCommand command = new RegCommand(stateManager);

        CommandResponse response = command.execute(123L, new String[]{});
        assertNotNull(response, "Команда должна возвращать ответ, а не null");
    }
}