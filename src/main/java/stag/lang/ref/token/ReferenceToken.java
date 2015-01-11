package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class ReferenceToken implements Token {
	private final Token parent;
	private final short[] references;
	private int cur = 0;
	public ReferenceToken(Token parent, int references) {
		this.parent = parent;
		this.references = new short[references];
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
		App.LOG.entering(getClass().getName(), "getBytes");
		final ByteBuffer buf = ByteBuffer.allocate(2 * this.references.length);
		
		for(short reference : this.references) {
			buf.putShort(reference);
		}

		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buf.array()));
		return buf.array();
	}

	@Override
	public Token handle(String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		
		ParseException e = new ParseException("Illegal token: " + sval, -1);
		App.LOG.throwing(getClass().getName(), "handle", e);
		throw e;
	}

	@Override
	public Token handle(double nval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", nval);

		this.references[this.cur] = (short) nval;
		this.cur++;
		
		Token next = this;
		if(this.cur == this.references.length) {
			next = this.parent;
		}
		
		App.LOG.exiting(getClass().getName(), "handle", next);
		return next;
	}

}
