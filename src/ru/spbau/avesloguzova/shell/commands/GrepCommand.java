/**
 * 
 */
package ru.spbau.avesloguzova.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import ru.spbau.avesloguzova.shell.ICommand;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class GrepCommand implements ICommand {

    @Override
    public String getName() {
	return "grep";
    }

    @Override
    public void execute(ArrayList<String> args, BufferedReader reader,
	    Writer writer) throws IOException {
	if (args.size() < 2) {
	    writer.write(getMan());
	    return;
	}
	String filename = args.get(args.size() - 1);
	String expression = args.get(args.size() - 2);

	boolean startsWith = expression.startsWith("^");
	boolean endsWith = expression.endsWith("&");
	expression = expression.substring(startsWith ? 1 : 0,
		expression.length() - (endsWith ? 1 : 0));

	Iterator<String> options = args.subList(0, args.size() - 2).iterator();
	boolean i = false;
	boolean w = false;
	int n = 0;
	while (options.hasNext()) {
	    String option = options.next();
	    switch (option) {
	    case "-i":
		i = true;
		break;
	    case "-w":
		w = true;
		break;
	    case "-A":
		if (!options.hasNext()) {
		    writer.write("Invalid option " + option);
		    writer.write(System.lineSeparator());
		    writer.write(getMan());
		}
		try {
		    n = Integer.valueOf(options.next());
		    if (n < 0) {
			writer.write("n should be >= 0");
		    }
		} catch (NumberFormatException e) {
		    writer.write("Invalid number format");
		}
		break;

	    default:
		writer.write("Undefined option " + option);
		writer.write(System.lineSeparator());
		writer.write(getMan());
		return;
	    }
	}

	try {
	    if (i) {
		expression = expression.toLowerCase();
	    }
	    List<String> fileContent = Files.readAllLines(Paths.get(filename));
	    if (i) {
		fileContent = fileContent.stream().map(l -> l.toLowerCase())
			.collect(Collectors.toList());
	    }

	    Iterator<String> lines = fileContent.iterator();
	    while (lines.hasNext()) {
		boolean ok = true;
		String line = lines.next();
		if (startsWith) {
		    ok = ok && line.startsWith(expression);
		    if (endsWith) {
			ok = ok && line.equals(expression);
		    }
		    if (w) {
			ok = ok && line.startsWith(expression + " ");
		    }
		}
		if (endsWith) {
		    ok = ok && line.endsWith(expression);
		    if (w) {
			ok = ok && line.endsWith(" " + expression);
		    }
		}
		if (((startsWith || endsWith) && ok)
			|| (" " + line + " ").indexOf(w ? " " + expression
				+ " " : expression) != -1) {
		    writer.write(line + System.lineSeparator());
		    for (int idx = 0; idx < n && lines.hasNext(); idx += 1) {
			writer.write(lines.next() + System.lineSeparator());
		    }
		}
	    }
	    writer.flush();
	} catch (IOException e) {
	    writer.write("Failed to read file " + filename);
	}

    }

    @Override
    public String getMan() {
	return "Prints filtered file content. sUsage:" + System.lineSeparator()
		+ getName() + " [options] expression filename"
		+ System.lineSeparator() + "Options: -i, -w, -A n"
		+ System.lineSeparator() + "Expression: [^]anyText[&]";
    }
}
