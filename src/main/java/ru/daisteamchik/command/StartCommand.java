package ru.daisteamchik.command;

public class StartCommand implements Command {

    public String getName(){
        return "/start";
    }

    public String getDescription(){
        return "Команда запускает бота и выводит приветственное сообщение";
    }

    public String execute(String[] args){
        return "Привет! 🎮 Добро пожаловать в DaiSteamchik — мини-соцсеть для быстрого и точного поиска тиммейтов в Steam!\n\n" +
                "Мы устраняем проблему долгого ручного поиска на форумах и в открытых чатах. " +
                "Бот автоматически сопоставит тебя с идеальными напарниками, учитывая:\n" +
                "🔹 Игровые дисциплины и твои любимые роли\n" +
                "🔹 Прайм-тайм\n" +
                "🔹 Текущий ранг\n" +
                "🔹 Trust Factor (Репутацию)\n\n" +
                "С чего начать?\n" +
                "Отправь /help, чтобы посмотреть список всех доступных команд.\n" +
                "Отправь /about, чтобы узнать концепцию проекта.";
    }
}
