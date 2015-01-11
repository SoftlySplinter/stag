package stag.lang.ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;

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
	private StreamTokenizer tokeniser;
	
	public Lexer(String source) {
		App.LOG.entering(getClass().getName(), "<init>", source);

		this.source = source;
		
		App.LOG.exiting(getClass().getName(), "<init>");
	}
	
	public final Token parse() throws IOException, ParseException {
		App.LOG.entering(getClass().getName(), "parse");
		
		final String className = source.replace(".stag", "");
		App.LOG.fine(String.format("Resolved class name as '%s'", className));
		
		final File sourceFile = new File(source);
		App.LOG.finest("Opening UTF-8 file " + sourceFile.getAbsolutePath());
		
		Token tokens = null;
		try(BufferedReader reader = Files.newBufferedReader(sourceFile.toPath(), Charset.forName("UTF-8"))) {
			this.tokeniser = new StreamTokenizer(reader);
			this.tokeniser.quoteChar('"');
			this.tokeniser.eolIsSignificant(false);
			this.tokeniser.wordChars(',', ',');
			tokens = doParse(new InitialToken());
		} catch(IOException e) {
			App.LOG.throwing(getClass().getName(), "parse", e);
			throw e;
		}
		
		App.LOG.exiting(getClass().getName(), "parse", tokens);
		return tokens;
	}

	private Token doParse(Token next) throws IOException, ParseException {
		App.LOG.entering(getClass().getName(), "doParse", next);
		int tok = tokeniser.nextToken();
		App.LOG.finest(String.format("Token: %d, sval: %s, nval: %f", tok, tokeniser.sval, tokeniser.nval));
		switch(tok) {
		case StreamTokenizer.TT_EOF:
			App.LOG.exiting(getClass().getName(), "doParse", next);
			return next;
		case StreamTokenizer.TT_WORD:
		case '"':
			next = next.handle(this.tokeniser.sval);
			break;
		case StreamTokenizer.TT_NUMBER:
			next = next.handle(this.tokeniser.nval);
			break;
		}
		
		final Token ret = doParse(next);
		App.LOG.exiting(getClass().getName(), "doParse", ret);
		return ret;
	}
}
