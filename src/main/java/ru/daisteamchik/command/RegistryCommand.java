package ru.daisteamchik.command;


import ru.daisteamchik.registration.RegistrationHandler;

public class RegistryCommand implements Command {

    private final RegistrationHandler handler;

    public RegistryCommand(RegistrationHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "/registry";
    }

    @Override
    public String getDescription() {
        return "Начать регистрацию";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        return new CommandResponse(handler.start(userId));
    }
}