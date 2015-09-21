package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import ru.spbau.avesloguzova.shell.ICommand;

import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;

/**
 */
public class WcCommand implements ICommand{
    @Override
    @NotNull
    public String getName() {
        return "wc";
    }

    @Override
    public void execute(@NotNull ArrayList<String> args, @NotNull PrintStream reader, @NotNull Writer writer) {

    }

    @Override
    @Nullable
    public String getMan() {
        return null;
    }
}
