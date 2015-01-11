package stag.lang.ref.token;

import java.text.ParseException;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;

public class UTF8Token implements Token {
	private String value;
	
	private Token parent;
	
	public UTF8Token(Token parent) {
		this.parent = parent;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Collection<Token> getChildren() {
		return null;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		return this.value.getBytes();
	}

	@Override
	public Token handle(String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		this.value = sval;
		
		App.LOG.exiting(getClass().getName(), "handle", this.parent);
		return this.parent;
	}

	@Override
	public Token handle(double nval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", nval);
		App.LOG.exiting(getClass().getName(), "handle", this);
		return this;
//		ParseException e = new ParseException("Illegal token " + nval, -1);
//		App.LOG.throwing(getClass().getName(), "handle", e);
//		throw e;
	}

}
