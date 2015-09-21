package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import ru.spbau.avesloguzova.shell.ICommand;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;

/**
 */
public class EchoCommand implements ICommand {
    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public void execute(@NotNull ArrayList<String> args, @NotNull PrintStream reader, @NotNull Writer writer) throws IOException {
        writer.write(reader.readLine());
    }

    @Override
    public String getMan() {
        return null;
    }
}
