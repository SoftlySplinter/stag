package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class ConstPoolToken implements Token {
	private final List<Token> constants = new ArrayList<Token>(); 
	private final Token parent;
	
	public ConstPoolToken(Token parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean hasChildren() {
		return !constants.isEmpty();
	}

	@Override
	public Collection<Token> getChildren() {
		return constants;
	}
	
	public short getLength() {
		return (short) (constants.size() + 1);
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(this.getClass().getName(), "getBytes");
		final ByteBuffer buffer = ByteBuffer.allocate(2);
		
		// Note: the const pool size is 1 above what it should be.
		buffer.putShort(this.getLength());
		
		App.LOG.exiting(this.getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	@Override
	public Token handle(String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		
		switch(sval) {
		case "end":
			App.LOG.exiting(getClass().getName(), "handle", this.parent);
			return this.parent;
		default:
			ParseException e = new ParseException("Illegal Token: " + sval, -1);
			App.LOG.throwing(getClass().getName(), "handle", e);
			throw e;
		}
	}
	
	@Override
	public Token handle(double nval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", nval);
		
		int index = (int) nval;
		Token t = new ConstantToken(this);
		
		this.constants.add(index - 1, t);
		
		App.LOG.exiting(getClass().getName(), "handle", t);
		return t;
	}
}
