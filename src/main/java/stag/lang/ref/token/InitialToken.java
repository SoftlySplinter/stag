package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;

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
		if(!this.hasChildren()) {
			ParseException classNotFound = new ParseException("Did not find class token.", -1);
			App.LOG.throwing(this.getClass().getName(), "getBytes", classNotFound);
			throw classNotFound;
		}
		
		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(MAGIC_NUMBER);
		return buffer.array();
	}

	@Override
	public boolean handle(int token) {
		App.LOG.entering(getClass().getName(), "handle", token);
		boolean ret = false;
		if(this.hasChildren()) {
			App.LOG.fine("Delegating to child class token");
			ret = this.classToken.handle(token);
		} else {
			final String next = (this.curToken + (char) token).trim();
			if (!this.curToken.equals(next)) {
				this.curToken = next;
				App.LOG.fine("Current token value: " + this.curToken);

				if (this.curToken.equals(".class")) {
					App.LOG.fine("Found class token");
					this.classToken = new ClassToken();
				}
			}
		}
		App.LOG.exiting(getClass().getName(), "handle", ret);
		return ret;
	}

}
