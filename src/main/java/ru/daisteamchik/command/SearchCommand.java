package ru.daisteamchik.command;

import ru.daisteamchik.handler.SearchHandler;

public class SearchCommand implements Command {
    private SearchHandler handler;

    public SearchCommand(SearchHandler searchHandler) {
    }

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
