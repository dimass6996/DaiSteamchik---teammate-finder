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
    public CommandResponse execute(long userId,String[] args) {

        if (args.length > 0) {
            String name = args[0].toLowerCase();

            if (!name.startsWith("/")) {
                name = "/" + name;
            }

            Command command = registry.getCommand(name);

            if (command == null) {
                return new CommandResponse("Команда " + name + " не найдена.");
            }

            return new CommandResponse(
                    command.getName() + " — " + command.getDescription()
            );
        }

        StringBuilder answer = new StringBuilder("Список доступных команд:\n\n");

        for (Command command : registry.getAllCommands()) {
            answer.append(command.getName())
                    .append(" — ")
                    .append(command.getDescription())
                    .append("\n");
        }

        return new CommandResponse(answer.toString());
    }

}