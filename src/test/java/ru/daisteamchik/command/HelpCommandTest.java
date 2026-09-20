package ru.daisteamchik.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelpCommandTest{

    @Test
    void testHelpCommand(){

        CommandRegistry registry = new CommandRegistry();
        HelpCommand helpCommand = new HelpCommand(registry);

        registry.register(helpCommand);

        String result = helpCommand.execute(new String[]{});

        assertNotNull(result);
        assertTrue(result.contains("/help"));
        assertTrue(result.contains("Выводит список всех команд"));
    }   
}