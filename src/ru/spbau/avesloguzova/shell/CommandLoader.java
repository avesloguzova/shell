package ru.spbau.avesloguzova.shell;


import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Class loads information about commands from configuration
 */
public class CommandLoader {
    public static final String DEAFAULT_CONFIG = "commands.xml";


    private List<ICommand> commands;

    public List<ICommand> loadCommands() throws IOException {
        //TODO
        return commands;
    }

    public boolean isCommandsLoaded() {
        return commands == null;
    }

    @Nullable
    public Collection<ICommand> getLastLoadedCommands() {
        return commands;
    }


}
