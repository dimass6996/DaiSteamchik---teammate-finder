package ru.daisteamchik.command;

import ru.daisteamchik.model.User;
import ru.daisteamchik.registration.RegistrationHandler;
import ru.daisteamchik.repository.UserRepository;

public class ProfileCommand implements Command {

    private final UserRepository userRep = new UserRepository();


    @Override
    public String getName() {
        return "/profile";
    }

    @Override
    public String getDescription() {
        return "Посмотреть свою сохраненную анкету";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        User user = userRep.getUserFromBd(userId);
        if (user == null) {
            return new CommandResponse("У тебя еще нет анкеты. Напиши /reg, чтобы зарегистрироваться.");
        }

        String text = "Твоя анкета:\n\n" + user.toString();

        return new CommandResponse(text);
    }
}