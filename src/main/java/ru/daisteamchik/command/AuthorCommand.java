package ru.daisteamchik.command;

public class AuthorCommand implements Command {
    @Override
    public String getName() { return "/author"; }

    @Override
    public String getDescription() { return "Информация об авторах проекта"; }

    @Override
    public String execute(String[] args) {
        return "Авторы проекта DaiSteamchik:\n" +
                "Бобешко Данил | @ply3a\n" +
                "Тихонов Дмитрий | @dimass6996\n\n" +
                "О нас:\n" +
                "Мы являемся студентами 2 курса по направлению \"Компьютерная безопасность\" " +
                "в Уральском федеральном университете имени первого Президента России Б. Н. Ельцина.\n\n" +
                "Данный проект создан в рамках учебного курса по объектно ориентированному программированию.";
    }
}