/**
 * 
 */
package ru.spbau.avesloguzova.shell.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import ru.spbau.avesloguzova.shell.Command;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class GrepCommand implements Command {

    @Override
    public String getName() {
	return "grep";
    }

    @Override
    public void execute(String[] args, InputStream inputStream,
	    OutputStream outputStream) throws IOException {
	boolean iOption = false;
	boolean wOption = false;
	int additionalLines = 0;

	int parsedArgsCount = 0;
	for (int idx = 0; idx < args.length; idx += 1) {
	    String curArg = args[idx];
	    if (curArg.equals("-i")) {
		iOption = true;
		continue;
	    }
	    if (curArg.equals("-w")) {
		wOption = true;
		continue;
	    }
	    if (curArg.equals("-a") && idx + 1 < args.length) {
		idx += 1;
		additionalLines = Integer.parseInt(args[idx]);
		continue;
	    }

	    args[parsedArgsCount] = args[idx];
	    parsedArgsCount += 1;
	}
	doGrep(new OutputStreamWriter(outputStream), iOption, wOption,
		additionalLines, Arrays.copyOf(args, parsedArgsCount),
		new InputStreamReader(inputStream));
    }

    @Override
    public String getMan() {
	return "Grep finds expression in files or in input"
		+ System.lineSeparator();
    }

    private void doGrep(Writer out, boolean iOption, boolean wOption,
	    int additionalLines, String[] parsedArgs, Reader in)
	    throws IOException, FileNotFoundException {
	if (parsedArgs.length == 0) {
	    throw new RuntimeException("No expression provided");
	}
	String expression = parsedArgs[parsedArgs.length - 1];
	if (parsedArgs.length == 1) {
	    doGrepSingleInput(out, in, iOption, wOption, additionalLines,
		    expression);
	    return;
	}
	for (int idx = 0; idx < parsedArgs.length - 1; idx += 1) {
	    String filename = parsedArgs[idx];
	    Reader inputFile = new FileReader(filename);
	    out.write("File: " + filename);
	    out.write(System.lineSeparator());
	    doGrepSingleInput(out, inputFile, iOption, wOption,
		    additionalLines, expression);
	}
    }

    private void doGrepSingleInput(Writer out, Reader input, boolean iOption,
	    boolean wOption, int additionalLines, String expression)
	    throws IOException {
	BufferedReader in = new BufferedReader(input);
	boolean starts = expression.startsWith("^");
	boolean ends = expression.endsWith("$");
	expression = expression.substring(starts ? 1 : 0, expression.length()
		- (ends ? 1 : 0));
	if (iOption) {
	    expression = expression.toLowerCase();
	}
	if (wOption) {
	    expression = " " + expression + " ";
	}
	String lineRed;
	while ((lineRed = in.readLine()) != null) {
	    String lineToCheck = iOption ? lineRed.toLowerCase() : lineRed;
	    if (wOption) {
		lineToCheck = " " + lineToCheck + " ";
	    }
	    if ((starts && !lineToCheck.startsWith(expression))
		    || (ends && !lineToCheck.endsWith(expression))
		    || (!lineToCheck.contains(expression))) {
		continue;
	    }
	    out.write(lineRed);
	    out.write(System.lineSeparator());
	    for (int i = 0; i < additionalLines; i += 1) {
		if ((lineRed = in.readLine()) == null) {
		    return;
		}
		out.write(lineRed);
		out.write(System.lineSeparator());
	    }
	}
    }

}
