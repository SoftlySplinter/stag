package stag.lang.ref.token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.Token;

public class AccessFlagToken implements Token {
	private final Token parent;
	private List<String> flags = new ArrayList<String>();

	public AccessFlagToken(Token parent) {
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
		return null;
	}

	@Override
	public Token handle(String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		
		this.flags.add(sval.replace(",", ""));
		
		Token next = parent;
		if(sval.endsWith(",")) {
			next = this;
		}
		
		App.LOG.exiting(getClass().getName(), "handle", next);
		return next;
	}

	@Override
	public Token handle(double nval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", nval);
		
		ParseException e = new ParseException("Illegal token: " + nval, -1);
		App.LOG.throwing(getClass().getName(), "handle", e);
		throw e;
	}
}
