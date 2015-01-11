package stag.lang.ref.token;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;

public class ConstantToken implements Token {
	private String type;
	private Token value;
	
	private final Token parent;

	public ConstantToken(Token parent) {
		this.parent = parent;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList(value);
	}

	@Override
	public byte[] getBytes() throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Token handle(String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		
		Token next;
		this.type = sval;
		switch(type) {
		case "utf8":
			next = new UTF8Token(this.parent);
			break;
		case "class":
		case "string":
			next = new ReferenceToken(this.parent, 1);
			break;
		case "descriptor":
		case "method":
		case "field":
			next = new ReferenceToken(this.parent, 2);
			break;
		default:
			throw new ParseException("Unknown type " + sval, -1);
		}
		
		this.value = next;
		
		App.LOG.exiting(getClass().getName(), "handle", next);
		return next;
	}

	@Override
	public Token handle(double nval) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
