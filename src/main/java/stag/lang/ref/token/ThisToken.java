package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class ThisToken implements Token {
	private short value = 0;
	private String curToken = "";
	private boolean finalised = false;

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList();
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		
		if(this.value < 1) {
			final ParseException e = new ParseException("", -1);
			App.LOG.throwing(this.getClass().getName(), "getBytes", e);
			throw e;
		}
		
		final ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(this.value);
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	@Override
	public boolean handle(final int token, final int offset) {
		App.LOG.entering(this.getClass().getName(), "handle", (char) token);
		if (!this.finalised) {
			if (this.curToken.isEmpty()) {
				if (!Character.isWhitespace(token)) {
					this.curToken += (char) token;
				}
			} else {
				if(Character.isWhitespace(token)) {
					this.finalised = true;
					this.value = Short.parseShort(curToken);
				}
			}
		}
		App.LOG.exiting(this.getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}

}
