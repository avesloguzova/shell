package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import ru.spbau.avesloguzova.shell.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 */
public class WcCommand implements Command {
    @Override
    @NotNull
    public String getName() {
        return "wc";
    }

    @Override
    public void execute(@NotNull String[] args, @NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {

    }

    @Override
    @Nullable
    public String getMan() {
        return "wc - print newline, word, and byte counts for each file";
    }
}
