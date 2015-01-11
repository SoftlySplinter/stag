package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class InitialToken implements Token {
	public static final int MAGIC_NUMBER = 0xCAFEBABE;
	private Token classToken = null;
	private String curToken = "";

	@Override
	public boolean hasChildren() {
		return this.classToken != null;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList(this.classToken);
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		if(!this.hasChildren()) {
			ParseException classNotFound = new ParseException("Did not find class token.", -1);
			App.LOG.throwing(this.getClass().getName(), "getBytes", classNotFound);
			throw classNotFound;
		}
		
		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(MAGIC_NUMBER);
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}
	
	@Override
	public Token handle(final String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		if(sval.equals("class")) {
			this.classToken = new ClassToken();

			App.LOG.exiting(getClass().getName(), "handle", sval);
			return this.classToken;
		} else {
			ParseException t = new ParseException("Illegal token: " + sval, -1); 
			App.LOG.throwing(getClass().getName(), "handle", t);
			throw t;
		}
	}
	
	@Override
	public Token handle(final double nval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", nval);
		App.LOG.exiting(getClass().getName(), "handle", this);
		return this;
	}
}
