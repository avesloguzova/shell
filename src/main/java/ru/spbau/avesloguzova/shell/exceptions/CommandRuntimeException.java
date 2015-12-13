package ru.spbau.avesloguzova.shell.exceptions;


public class CommandRuntimeException extends RuntimeException {
    public CommandRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
