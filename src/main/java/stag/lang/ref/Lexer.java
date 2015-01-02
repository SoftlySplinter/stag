package stag.lang.ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.token.InitialToken;
/**
 * Lexical Analyser for the Stag programming language.
 * 
 * @author Alexander D Brown
 * @version 1.0-SNAPSHOT
 */
public class Lexer {
	public final String source;
	
	public Lexer(String source) {
		App.LOG.entering(getClass().getName(), "<init>", source);

		this.source = source;
		
		App.LOG.exiting(getClass().getName(), "<init>");
	}
	
	public final Token parse() throws IOException {
		App.LOG.entering(getClass().getName(), "parse");
		
		final String className = source.replace(".stag", "");
		App.LOG.fine(String.format("Resolved class name as '%s'", className));
		
		final File sourceFile = new File(source);
		App.LOG.finest("Opening UTF-8 file " + sourceFile.getAbsolutePath());
		
		try(BufferedReader reader = Files.newBufferedReader(sourceFile.toPath(), Charset.forName("UTF-8"))) {
			// Parse the file
			final Token tokens = parse(reader);
			App.LOG.exiting(getClass().getName(), "parse", tokens);
			return tokens;
		} catch(IOException e) {
			App.LOG.throwing(getClass().getName(), "parse", e);
			throw e;
		}
	}

	private final Token parse(BufferedReader reader) throws IOException {
		App.LOG.entering(getClass().getName(), "parse", reader);
		final Token token = new InitialToken();
		
		for(int tok = reader.read(); tok != -1; tok = reader.read()) {
			token.handle(tok);
		}
		
		App.LOG.exiting(getClass().getName(), "parse", token);
		return token;
	}
}
