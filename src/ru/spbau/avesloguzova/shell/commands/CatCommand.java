package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import ru.spbau.avesloguzova.shell.ICommand;

import java.io.*;
import java.util.ArrayList;

/**
 *
 */
public class CatCommand implements ICommand {
    @Override
    @NotNull
    public String getName() {
        return "cat";
    }

    @Override
    public void execute(@NotNull ArrayList<String> args, @NotNull PrintStream reader, @NotNull Writer writer) throws IOException {
        if (args.size() == 1) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(args.get(0)))) {
                copy(writer, fileReader);
            }
        } else {
            copy(writer, reader);
        }
    }


    private void copy(@NotNull Writer writer, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
        }
    }

    @Override
    @Nullable
    public String getMan() {
        return "Man for cat";
    }
}
