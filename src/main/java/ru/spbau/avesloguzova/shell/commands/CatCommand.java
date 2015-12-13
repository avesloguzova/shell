package ru.spbau.avesloguzova.shell.commands;

import ru.spbau.avesloguzova.shell.Command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 */
public class CatCommand implements Command {
    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public void execute(String[] args, InputStream inputStream, OutputStream outputStream) throws IOException {
        if (args.length == 1) {
            String fileName = args[0];
            Files.copy(Paths.get(fileName), outputStream);
        } else {
            new DataOutputStream(outputStream).writeUTF("Usage: cat [FILE]");
        }
    }


    @Override
    public String getMan() {
        return "cat - concatenate files and print on the standard output";
    }
}
