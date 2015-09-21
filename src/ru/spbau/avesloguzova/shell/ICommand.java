package ru.spbau.avesloguzova.shell;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Interface of shell command
 */
public interface ICommand {

    @NotNull
    String getName();

    void execute(@NotNull ArrayList<String> args, @NotNull BufferedReader reader, @NotNull Writer writer) throws IOException;

    @Nullable
    String getMan();
}
