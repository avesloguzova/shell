package ru.spbau.avesloguzova.shell;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface of shell command. Implementation should have constructor without parameters
 */
public interface Command {

    @NotNull
    String getName();

    void execute(@NotNull String[] args, @NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException;

    @Nullable
    String getMan();
}
