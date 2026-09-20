package ru.daisteamchik.command; // это чтобы компилятор смог понять полный путь к файлу

public interface Command {
    String getName();
    String getDescription();
    String execute(String[] args);  // создали интерфейс каждой команды, что-то типа h Файла в c++
                                    // подключается в классе через implements Command - класс
                                    // должен иметь поля, которые содержатся в интерфейсе обязательно
}

