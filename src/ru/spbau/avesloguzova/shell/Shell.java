package ru.spbau.avesloguzova.shell;

import ru.spbau.avesloguzova.shell.commands.ExitCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Shell {

    private final Map<String, ICommand> commandsMap = new HashMap<>();

    public Shell(CommandLoader loader) throws IOException {
        commandsMap.put(ExitCommand.NAME, new ExitCommand());
        putCommands(loader.loadCommands());
    }

    private void putCommands(List<ICommand> commandList) {
        for (ICommand command : commandList) {
            commandsMap.put(command.getName(), command);
        }
    }

    public void run() throws IOException {

    }

    private String[] splitByCommands(String line) {
        return line.split("\\|");
    }
}