package ru.daisteamchik.command;

import ru.daisteamchik.bot.Bot;
import ru.daisteamchik.service.BotState;

public class CommandResponse {
    private final String message;

    public CommandResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
