package ru.spbau.avesloguzova.shell.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import ru.spbau.avesloguzova.shell.Command;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.IntSummaryStatistics;
import java.util.List;

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
        int wc = 0;
        int lc = 0;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (String fileName : args) {
            List<String> fileLines = Files
                    .readAllLines(Paths.get(fileName));
            IntSummaryStatistics stats = fileLines.stream()
                    .mapToInt(line -> line.trim().split("\\s").length).summaryStatistics();
            wc += stats.getSum();
            lc += stats.getCount();
        }
        out.write(String.format("%d %d %d",
                args.length, lc, wc));
        out.newLine();
        out.flush();
    }

    @Override
    @Nullable
    public String getMan() {
        return "wc - print newline, word, and byte counts for each file";
    }
}
