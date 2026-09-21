package ru.daisteamchik.command;

public class HelpCommand implements Command {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry){
        this.registry = registry;
    }
    @Override
    public String getName(){
        return "/help";

    }

    @Override
    public String getDescription() {
        return "Выводит список всех команд или справку по конкретной: /help <команда>";
    }

    @Override
    public String execute(String[] args){

        if (args.length > 0){
            String name;
            if (args[0].startsWith("/")){
                name = args[0];  
            }else{
                name = "/" + args[0];

            }
            Command cmd = registry.getCommand(name);
            if (cmd != null){
                return cmd.getName() + " — " + cmd.getDescription();
            }else{
                return "Команда " + name + " не найдена.";
            }
        }else{
            StringBuilder answer = new StringBuilder("Список доступных команд:\n");
            for (Command cmd : registry.getAllCommands()){
                answer.append(cmd.getName()).append("-");
                answer.append(cmd.getDescription()).append("\n");
            }
            return answer.toString();
        }

    }

}