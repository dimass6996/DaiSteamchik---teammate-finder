package ru.daisteamchik.service;

import java.util.HashMap;
import java.util.Map;

public class TransitionTable {

    private static final Map<BotState, Map<BotEvent, BotState>> TABLE = new HashMap<>();

    private static void add(BotState from, BotEvent event, BotState to) {
        if (!TABLE.containsKey(from)) {
            TABLE.put(from, new HashMap<>());
        }
        TABLE.get(from).put(event, to);
    }

    static {

        // движение при команде /start или в будущем при команде вызывающей редактор профиля
        add(BotState.NONE,BotEvent.CMD_START,BotState.REG_WAITING_NAME);
        add(BotState.REG_WAITING_NAME,BotEvent.TEXT,BotState.REG_WAITING_STEAM_URL);
        add(BotState.REG_WAITING_STEAM_URL,BotEvent.TEXT,BotState.REG_WAITING_ROLE);
        add(BotState.REG_WAITING_ROLE,BotEvent.TEXT,BotState.REG_WAITING_PRIME_TIME);
        add(BotState.REG_WAITING_PRIME_TIME,BotEvent.TEXT,BotState.REG_WAITING_GAMES);

        // для вызова команды /back
        add(BotState.REG_WAITING_GAMES,BotEvent.CMD_BACK,BotState.REG_WAITING_PRIME_TIME);
        add(BotState.REG_WAITING_PRIME_TIME,BotEvent.CMD_BACK,BotState.REG_WAITING_ROLE);
        add(BotState.REG_WAITING_ROLE, BotEvent.CMD_BACK,BotState.REG_WAITING_STEAM_URL);
        add(BotState.REG_WAITING_STEAM_URL, BotEvent.CMD_BACK,BotState.REG_WAITING_NAME);
        add(BotState.REG_WAITING_NAME, BotEvent.CMD_BACK,BotState.NONE);
    }


    public static BotState next(BotState current, BotEvent event) {
        Map<BotEvent, BotState> inner = TABLE.get(current);
        if (inner == null) {
            return current;
        }
        BotState result = inner.get(event);
        if (result == null) {
            return current;
        }
        return result;
    }
}