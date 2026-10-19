package ru.daisteamchik.command;


import ru.daisteamchik.registration.RegistrationHandler;

public class CancelCommand implements Command {

    private final RegistrationHandler handler;

    public CancelCommand(RegistrationHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "/cancel";
    }

    @Override
    public String getDescription() {
        return "Отменить текущую операцию";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        if (!handler.isRegistering(userId)) {
            return new CommandResponse("Сейчас нечего отменять!");
        }

        handler.cancel(userId);
        return new CommandResponse("Регистрация отменена!");
    }
}