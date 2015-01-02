package stag.lang.comp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;

import stag.lang.App;
import stag.lang.ref.Lexer;
import stag.lang.ref.Token;

/**
 * A compiler for the Stag language.
 * <p>
 * Compiles Stag source code into a Java class format which can be run directly
 * using the JVM.
 * 
 * @author Alexander Brown
 * @version 1.0-SNAPSHOT
 */
public class Compiler {
	public final Lexer lexer;

	public Compiler(Lexer lexer) {
		App.LOG.entering(getClass().getName(), "<init>", lexer);
		this.lexer = lexer;
		App.LOG.exiting(getClass().getName(), "<init>");
	}

	public final void compile() throws IOException, ParseException {
		App.LOG.entering(getClass().getName(), "compile");

		// TODO Consider arguments, etc.
		final String classFileName = this.lexer.source.replace(".stag", ".class");
		App.LOG.finest(String.format("Resolved class file for source '%s' as '%s'", this.lexer.source, classFileName));

		final File classFile = new File(classFileName);
		App.LOG.finest(String.format("Opening file '%s' for writing", classFile.getAbsoluteFile()));
		
		final Token tokens = lexer.parse();

		try (OutputStream out = Files.newOutputStream(classFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			compile(tokens, out);
		}

		App.LOG.exiting(getClass().getName(), "compile");
	}

	private void compile(Token tokens, OutputStream out) throws IOException, ParseException {
		out.write(tokens.getBytes());
		if(tokens.hasChildren()) {
			for(Token child : tokens.getChildren()) {
				compile(child, out);
			}
		}
	}
}
