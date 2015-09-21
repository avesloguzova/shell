package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import ru.spbau.avesloguzova.shell.ICommand;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;

/**
 */
public class ExitCommand implements ICommand {

    public static final String NAME = "exit";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(@NotNull ArrayList<String> args, @NotNull PrintStream reader, @NotNull Writer writer) throws IOException {
        System.exit(0);
    }

    @Override
    public String getMan() {
        return null;
    }
}
