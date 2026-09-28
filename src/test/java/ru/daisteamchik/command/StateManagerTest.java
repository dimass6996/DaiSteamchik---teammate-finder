package ru.daisteamchik.command;

import org.junit.jupiter.api.Test;
import ru.daisteamchik.service.*;

import static org.junit.jupiter.api.Assertions.*;

public class StateManagerTest{

    @Test
    void testStateManager(){

        StateManager manager = new StateManager();

        assertTrue(manager.getState(123).equals(BotState.NONE));
        manager.setState(123,BotState.REG_WAITING_NAME);
        assertTrue(manager.getState(123).equals(BotState.REG_WAITING_NAME));
        assertTrue(manager.next(123,BotEvent.TEXT).equals(BotState.REG_WAITING_STEAM_URL));
        assertTrue(manager.next(123,BotEvent.CMD_BACK).equals(BotState.REG_WAITING_NAME));
    }
}