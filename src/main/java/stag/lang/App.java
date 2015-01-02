package stag.lang;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import stag.lang.comp.Compiler;
import stag.lang.ref.Lexer;

/**
 * 
 * @author Alexander D Brown
 * @version 1.0-SNAPSHOT
 */
public class App {
	public static final Logger LOG = Logger.getLogger("stag");

	static {
//		ConsoleHandler handler = new ConsoleHandler();
//		handler.setLevel(Level.ALL);
		LOG.setLevel(Level.ALL);
//		LOG.addHandler(handler);
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		LOG.entering(App.class.getName(), "main", args);
		
		if(args.length < 1) {
			LOG.severe("No source file specified.");
			usage();
			
			LOG.info("Exiting with RC 255");
			System.exit(255);
		}
		
		final String source = args[args.length - 1];
		LOG.fine("Source file: " + source);
		
		final Lexer lexer = new Lexer(source);
		final Compiler compiler = new Compiler(lexer);
		compiler.compile();
		
		LOG.exiting(App.class.getName(), "main");
	}

	private static void usage() {
		LOG.entering(App.class.getName(), "usage");
		System.out.printf("Usage: stag [args] source\n");
		LOG.exiting(App.class.getName(), "usage");
	}
}
