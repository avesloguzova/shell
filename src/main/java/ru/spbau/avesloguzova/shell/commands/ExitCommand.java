package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import ru.spbau.avesloguzova.shell.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 */
public class ExitCommand implements Command {

    public static final String NAME = "exit";
    private static final ExitCommand INSTANCE = new ExitCommand();

    public static ExitCommand getInstance() {
        return INSTANCE;
    }

    private ExitCommand() {

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(@NotNull String[] args, @NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        System.exit(0);
    }

    @Override
    public String getMan() {
        return null;
    }
}
