package stag.lang;

import java.util.ArrayList;
import java.util.List;

import stag.lang.ref.Reference;
/**
 * Lexical Analyser for the Stag programming language.
 * 
 * @author Alexander D Brown
 * @version 1.0-SNAPSHOT
 */
public class Lexer {
	/** The references in a source file */
	public final List<Reference> references;
	
	public Lexer() {
		App.LOG.entering(getClass().getName(), "<init>");
		this.references = new ArrayList<Reference>();
		App.LOG.exiting(getClass().getName(), "<init>");
	}

	public static Lexer analyse(String sourceFile) {
		App.LOG.entering(Lexer.class.getName(), "analyse", sourceFile);
		Lexer lexer = null;
		
		App.LOG.exiting(Lexer.class.getName(), "analyse", lexer);
		return lexer;
	}
}