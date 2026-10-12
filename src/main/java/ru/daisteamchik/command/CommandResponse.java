package ru.daisteamchik.command;

import ru.daisteamchik.bot.Bot;
import ru.daisteamchik.service.BotState;

public class CommandResponse {
    private final String message;
    private final BotState nextState;

    public CommandResponse(String message){
        this.message = message;
        this.nextState = null;
    }

    public CommandResponse(String message, BotState nextState){
        this.message = message;
        this.nextState = nextState;
    }

    public String getMessage() {
        return message;
    }

    public BotState getState(){
        return nextState;
    }
}
