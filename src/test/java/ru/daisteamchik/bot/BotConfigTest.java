package ru.daisteamchik.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import static org.junit.jupiter.api.Assertions.*;

class BotConfigTest {
    @Test
    void connectsDirectlyWhenProxyIsNotConfigured() {
        assertEquals(DefaultBotOptions.ProxyType.NO_PROXY,
                BotConfig.createBotOptions(null, null).getProxyType());
        assertEquals(DefaultBotOptions.ProxyType.NO_PROXY,
                BotConfig.createBotOptions(" ", "").getProxyType());
    }

    @Test
    void configuresHttpProxy() {
        DefaultBotOptions options = BotConfig.createBotOptions(" 127.0.0.1 ", " 12334 ");
        assertEquals(DefaultBotOptions.ProxyType.HTTP, options.getProxyType());
        assertEquals("127.0.0.1", options.getProxyHost());
        assertEquals(12334, options.getProxyPort());
    }

    @Test
    void rejectsIncompleteProxyConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> BotConfig.createBotOptions("localhost", null));
        assertThrows(IllegalArgumentException.class, () -> BotConfig.createBotOptions(null, "12334"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "65536", "not-a-port", "999999999999"})
    void rejectsInvalidProxyPort(String port) {
        assertThrows(IllegalArgumentException.class, () -> BotConfig.createBotOptions("localhost", port));
    }
}
