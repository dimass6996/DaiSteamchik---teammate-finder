package ru.daisteamchik.command;

import ru.daisteamchik.service.BotEvent;
import ru.daisteamchik.service.StateManager;

public class RegCommand implements Command {

    private final StateManager stateManager;

    public RegCommand(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public String getName() {
        return "/reg";
    }

    @Override
    public String getDescription() {
        return "Начать регистрацию для поиска тиммейтов";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        stateManager.next(userId, BotEvent.CMD_REG);

        String text = "Привет! Давай заполним анкету для поиска тиммейтов.\n\n" +
                "1: Как тебя зовут?";
        return new CommandResponse(text);
    }
}