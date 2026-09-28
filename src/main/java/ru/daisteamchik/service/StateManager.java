package ru.daisteamchik.service;

import java.util.HashMap;
import java.util.Map;

public class StateManager {

    private Map<Long, BotState> states = new HashMap<>();

    public BotState getState(long chatId){
        return states.getOrDefault(chatId,BotState.NONE);
    }

    public void setState(long chatId,BotState state){
        states.put(chatId,state);
    }

    public BotState next(long chatId,BotEvent event){
        BotState current = getState(chatId);
        BotState next = TransitionTable.next(current,event);
        setState(chatId,next);
        return next;
    }

}
