package ru.daisteamchik.command;

import ru.daisteamchik.bot.Bot;
import ru.daisteamchik.service.BotState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class CommandResponse {
    private final String message;
    private final InlineKeyboardMarkup keyboard;

    public CommandResponse(String message) {
        this.message = message;
        this.keyboard = null;
    }

    public CommandResponse(String message, InlineKeyboardMarkup keyboard) {
        this.message = message;
        this.keyboard = keyboard;
    }

    public String getMessage() {
        return message;
    }

    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }
}