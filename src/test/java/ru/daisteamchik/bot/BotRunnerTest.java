package ru.daisteamchik.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;

class BotRunnerTest {

    @Test
    void reportsErrorWithoutToken() {
        Throwable error = new TelegramApiException("Ошибка запуска",
                new IllegalStateException("Ошибка сети с секретным токеном"));

        String message = BotRunner.describeError(error, "секретный токен");

        assertTrue(message.contains("Ошибка сети"));
        assertFalse(message.contains("секретный токен"));
    }
}
