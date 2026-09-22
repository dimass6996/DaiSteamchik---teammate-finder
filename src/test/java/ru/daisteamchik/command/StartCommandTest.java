package ru.daisteamchik.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StartCommandTest{

    @Test
    void testStartCommand(){

        StartCommand startCommand = new StartCommand(); // создаю тестовый объект HelpCommand

        String result = startCommand.execute(new String[]{}); // вызываю команду help с пустыми аргументами

        assertNotNull(result);//проверяю что команда что-то вернула вообще
        assertTrue(result.contains("Привет!"));// проверяю есть ли там нужное имя команды
    }
}