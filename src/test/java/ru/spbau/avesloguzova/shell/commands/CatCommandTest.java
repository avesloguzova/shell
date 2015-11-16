package ru.spbau.avesloguzova.shell.commands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CatCommandTest {
    //    public static final String CATTESTFILE_TXT = "cattestfile.txt";
    CatCommand testCommand = new CatCommand();

    @Test
    public void nameTest() {
        assertEquals(testCommand.getName(), "cat");
    }

}