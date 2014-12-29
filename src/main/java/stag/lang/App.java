package stag.lang;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Alexander D Brown
 * @version 1.0-SNAPSHOT
 */
public class App {
	public static final Logger LOG = Logger.getLogger("stag");

	static {
		LOG.setLevel(Level.ALL);
	}
	
	public static void main(String[] args) {
		LOG.entering(App.class.getName(), "main", args);
		
		if(args.length < 1) {
			LOG.severe("No source file specified.");
			usage();
			
			LOG.info("Exiting with RC 255");
			System.exit(255);
		}
		
		final String source = args[args.length - 1];
		LOG.fine("Source file: " + source);
		
		Lexer lexer = Lexer.analyse(source);
		
		
		LOG.exiting(App.class.getName(), "main");
	}

	private static void usage() {
		LOG.entering(App.class.getName(), "usage");
		System.out.printf("Usage: stag [args] source\n");
		LOG.exiting(App.class.getName(), "usage");
	}
}
