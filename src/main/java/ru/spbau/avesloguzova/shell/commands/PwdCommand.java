package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import ru.spbau.avesloguzova.shell.Command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PwdCommand implements Command {
    @Override
    public String getName() {
        return "pwd";
    }

    @Override
    public void execute(@NotNull String[] args, @NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
        new DataOutputStream(outputStream).writeUTF(System.getProperty("user.dir"));
    }

    @Override
    public String getMan() {
        return null;
    }
}
