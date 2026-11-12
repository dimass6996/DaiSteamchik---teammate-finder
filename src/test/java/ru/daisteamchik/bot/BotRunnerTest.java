package ru.daisteamchik.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;

class BotRunnerTest {
    @Test
    void reportsNestedNetworkFailure() {
        Throwable error = new TelegramApiException("Error removing old webhook",
                new TelegramApiException("Unable to execute deletewebhook method",
                        new SocketException("Network is unreachable")));

        String message = BotRunner.describeError(error, "test-token");

        assertTrue(message.contains("Error removing old webhook"));
        assertTrue(message.contains("SocketException: Network is unreachable"));
    }

    @Test
    void hidesTokenInEveryErrorMessage() {
        Throwable error = new TelegramApiException("Request failed for bot-test-token",
                new IllegalStateException("https://api.telegram.org/bot-test-token/getMe"));

        String message = BotRunner.describeError(error, "test-token");

        assertFalse(message.contains("test-token"));
        assertTrue(message.contains("[TOKEN]"));
    }
}
