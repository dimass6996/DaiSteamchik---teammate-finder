package ru.daisteamchik.command;

import java.util.Collection;
import java.util.HashMap;  // импортирую все структуры которые нужны будут
import java.util.Map;

public class CommandRegistry {  // класс коллекции которая будет хранить все наши команды

    private final Map<String,Command> commands = new HashMap<>();

    public void register(Command command){ // метод с помощью которого можно добавить новую команду в реестр комманд
        commands.put(command.getName(),command);
    }

    public Command getCommand(String name){ // метод по которому можно получить конкретную команду из реестра
        return commands.get(name);
    }

    public Collection<Command> getAllCommands(){ // метод по которому можно получить все команды
        return commands.values(); // command.values() возвращает коллекцию,
                                  // поэтому возвращаемое значение Collection<Command>
    }
}