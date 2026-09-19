package ru.daisteamchik.command

import java.util.Collection;
import java.util.HashMap;  // импортирую все структуры которые нужны будут
import java.util.Map;

public class CommandRegistry {  // класс коллекции которая будет хранить все наши комманды

    private final Map<String,Command> commands = new HashMap<>();

    public void register(Command command){ // метод с помощью которого можно добавить новую комманду в реестр комманд
        commands.put(command.getName,command);
    }

    public Command getCommand(String name){ // метод по которому можно получить конкретную комманду из реестра
        return commands.getName();
    }

    public Collection<Command> getAllCommands(){ // метод по которому можно получить все все команды
        return commands.values(); // command.values() возвращает коллекцию,
                                  // поэтому возвращаемое значение Collection<Command>
    }
}