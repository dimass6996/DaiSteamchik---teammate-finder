package ru.daisteamchik.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import static org.junit.jupiter.api.Assertions.*;

class BotConfigTest {

    @Test
    void worksWithoutProxy() {
        DefaultBotOptions options = BotConfig.createBotOptions(null, null);

        assertEquals(DefaultBotOptions.ProxyType.NO_PROXY, options.getProxyType());
    }

    @Test
    void configuresProxy() {
        DefaultBotOptions options = BotConfig.createBotOptions("127.0.0.1", "12334");

        assertEquals(DefaultBotOptions.ProxyType.HTTP, options.getProxyType());
        assertEquals("127.0.0.1", options.getProxyHost());
        assertEquals(12334, options.getProxyPort());
    }

    @Test
    void rejectsInvalidProxy() {
        assertThrows(IllegalArgumentException.class,
                () -> BotConfig.createBotOptions("127.0.0.1", "не число"));
    }
}
