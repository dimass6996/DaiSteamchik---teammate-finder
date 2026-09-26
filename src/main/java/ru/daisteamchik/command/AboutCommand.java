package ru.daisteamchik.command;

public class AboutCommand implements Command {
    @Override
    public String getName() { return "/about"; }

    @Override
    public String getDescription() { return "Описание концепции проекта DaiSteamchik"; }

    @Override
    public String execute(String[] args) {
        return "🎮 DaiSteamchik: Teammate Finder\n" +
                "Платформа для быстрого поиска тиммейтов для совместных игр в Steam.\n\n" +
                "Возможности:\n" +
                "• Подбор игроков по играм (CS2, Dota 2, Apex...)\n" +
                "• Фильтр по рангу и прайм-тайму\n" +
                "• Система репутации (Trust Factor)\n" +
                "• LFG-лобби и взаимные рекомендации\n\n" +
                "Используйте /help для списка всех команд.";
    }
}