package ru.daisteamchik.command;

import ru.daisteamchik.registration.RegistrationHandler;

public class BackCommand implements Command {

    private final RegistrationHandler handler;

    public BackCommand(RegistrationHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "/back";
    }

    @Override
    public String getDescription() {
        return "Вернуться на предыдущий шаг";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        if (handler.isRegistering(userId)) {
            return new CommandResponse(handler.back(userId));
        }

        return new CommandResponse("Сейчас некуда возвращаться!");
    }
}