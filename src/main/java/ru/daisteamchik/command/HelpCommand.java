package ru.daisteamchik.command

public class HelpCommand implements Command {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry){
        this.registry = registry;
    }
    @override
    String getName(){
        return "/help";

    }

    @Override
    public String getDescription() {
        return "Выводит список всех команд или справку по конкретной: /help <команда>";
    }

    
    String execute(String[] args);

}