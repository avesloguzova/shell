package ru.spbau.avesloguzova.shell;

import com.sun.istack.internal.NotNull;
import ru.spbau.avesloguzova.shell.commands.ExitCommand;
import ru.spbau.avesloguzova.shell.exceptions.CommandRuntimeException;
import ru.spbau.avesloguzova.shell.exceptions.CommandsLoadingException;
import ru.spbau.avesloguzova.shell.exceptions.RunCommandException;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Simple implementation of shell. It uses all commands from commands.list loaded by default classloader
 */
public class Shell implements Closeable {

    private final Map<String, Command> commandsMap = new HashMap<>();
    private ExecutorService commandsThreadPool;

    public Shell(CommandLoader loader) throws IOException, CommandsLoadingException {
        addDefaultCommands();
        addLoadedCommands(loader.loadCommands());
    }

    public static void main(String[] args) throws IOException, CommandsLoadingException {
        new Shell(new CommandLoader()).run();
    }

    public void run() {
        try {
            commandsThreadPool = Executors.newCachedThreadPool();
            Scanner scaner = new Scanner(System.in);
            System.out.println("It is shell. You can type command.");
            do {
                try {
                    String line = scaner.nextLine();


                    if (line == null) System.exit(0);
                    if (line.isEmpty()) continue;

                    runCommands(splitByCommands(line));
                } catch (RunCommandException e) {
                    System.err.println("Command was terminated with errors.");
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    System.err.println("Can't read from command line.");
                }
            } while (true);
        } finally {
            commandsThreadPool.shutdown();
        }
    }

    private void addDefaultCommands() {
        commandsMap.put(ExitCommand.NAME, ExitCommand.getInstance());
        ManCommand manCommand = new ManCommand();
        commandsMap.put(ManCommand.NAME, manCommand);
    }

    private void addLoadedCommands(List<Command> commandList) {
        for (Command command : commandList) {
            commandsMap.put(command.getName(), command);
        }
    }

    private void runCommands(String[] commands) throws RunCommandException, IOException {
        InputStream[] readers = new InputStream[commands.length];
        OutputStream[] writers = new OutputStream[commands.length];

        readers[0] = System.in;
        writers[commands.length - 1] = System.out;

        try {
            for (int i = 0; i < commands.length - 1; i++) {
                PipedInputStream pipedInputStream = new PipedInputStream();
                readers[i + 1] = pipedInputStream;
                writers[i] = new PipedOutputStream(pipedInputStream);
            }

            for (int i = 0; i < commands.length; i++) {
                CommandExecutor commandExecutor = createCommandExecutor(commands[i].trim());
                commandExecutor.setInputStream(readers[i]);
                commandExecutor.setOutputStream(writers[i]);

                Future<?> future = commandsThreadPool.submit(commandExecutor);
                future.get();

            }
        } catch (InterruptedException e) {
            throw new RunCommandException("Command was interrupted.");
        } catch (ExecutionException e) {
            throw new RunCommandException(e.getCause().getMessage());
        } catch (IOException e) {
            throw new RunCommandException("Some I/O exception by using pipeline");
        } finally {
            for (int i = 0; i < commands.length - 1; i++) {
                readers[i + 1].close();
                writers[i].close();
            }
        }
    }

    private CommandExecutor createCommandExecutor(String commandString) throws RunCommandException {
        String[] splitCommand = splitCommand(commandString);
        Command command = commandsMap.get(splitCommand[0]);
        if (command == null) throw new RunCommandException("Command don't exist.");
        String[] args = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
        return new CommandExecutor(command, args);
    }

    private String[] splitCommand(String command) {
        return command.split("\\s+");
    }

    private String[] splitByCommands(String line) {
        return line.split("\\|");
    }

    @Override
    public void close() throws IOException {
        commandsThreadPool.shutdown();
    }

    private static class CommandExecutor implements Runnable {
        private final Command command;
        private final String[] args;
        private InputStream inputStream;
        private OutputStream outputStream;

        public CommandExecutor(Command command, String[] args) {
            this.command = command;
            this.args = args;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void setOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            try {
                command.execute(args, inputStream, outputStream);
                outputStream.flush();
            } catch (IOException e) {
                throw new CommandRuntimeException("Some I/O exception during the command " + command.getName(), e);

            }
        }
    }

    private class ManCommand implements Command {

        public static final String NAME = "man";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public void execute(@NotNull String[] args, @NotNull InputStream inputStream, @NotNull OutputStream outputStream) throws IOException {
            DataOutputStream out = new DataOutputStream(outputStream);
            if (args.length == 1) {
                Command command = commandsMap.get(args[0]);
                if (command != null) {
                    out.writeUTF(command.getMan());
                }
            } else {
                out.writeUTF("Usage: man [COMMAND NAME]");
            }
        }

        @Override
        public String getMan() {
            return "Manual page. Usage: man [COMMAND NAME]";
        }

    }

}