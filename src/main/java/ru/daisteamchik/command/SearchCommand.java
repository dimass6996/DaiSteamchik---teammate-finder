package ru.daisteamchik.command;

public class SearchCommand implements Command {


    @Override
    public String getName() {
        return "/search";
    }

    @Override
    public String getDescription() {
        return "Команда для поиска тиммейтов";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        return new CommandResponse(handler.start(userId));
    }

}
