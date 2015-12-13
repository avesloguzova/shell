package ru.spbau.avesloguzova.shell;


import ru.spbau.avesloguzova.shell.exceptions.CommandsLoadingException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class loads information about commands from configuration file. Default configuration file is commands.list
 */
public class CommandLoader {
    public static final String DEFAULT_CONFIG = "commands.list";

    private final String configFile;

    public CommandLoader(String configFile) {
        URL resource = getClass().getClassLoader().getResource(configFile);
        if (resource == null) throw new IllegalArgumentException("Configuration file doesn't exist.");

        this.configFile = resource.getFile();
    }

    public CommandLoader() {
        this(DEFAULT_CONFIG);
    }


    public List<Command> loadCommands() throws IOException, CommandsLoadingException {
        List<Command> commands = new ArrayList<>();

        List<String> fileLines = Files.readAllLines(Paths.get(configFile));
        try {
            for (String classPath : fileLines) {
                Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass(classPath);
                Object instance = aClass.newInstance();
                if (instance instanceof Command) {
                    commands.add((Command) instance);
                } else {
                    throw new CommandsLoadingException(String.format("Error in configuration file. " +
                            "Class %s isn't INSTANCE of Command", classPath));
                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new CommandsLoadingException("Can't load command. Possible error in configuration file");
        }
        return commands;
    }
}
