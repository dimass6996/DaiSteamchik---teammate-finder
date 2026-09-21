package ru.daisteamchik.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelpCommandTest{

    @Test
    void testHelpCommand(){

        CommandRegistry registry = new CommandRegistry(); // Создаю тестовый реестр команд
        HelpCommand helpCommand = new HelpCommand(registry); // создаю тестовый объект HelpCommand

        registry.register(helpCommand); // добавляю команду help command в тестовый реестр

        String result = helpCommand.execute(new String[]{}); // вызываю команду help с пустыми аргументами

        assertNotNull(result);//проверяю что команда что-то вернула вообще
        assertTrue(result.contains("/help"));// проверяю есть ли там нужное имя команды
        assertTrue(result.contains("Выводит список всех команд"));
    }   
}