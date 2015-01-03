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
	private boolean finalised = false;
	private Token currentDelegate;
	private short curIndex;
	private String curToken = "";

	public int getClassReference(String className) {
		// TODO Auto-generated method stub
		return 0;
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
	public boolean handle(final int token, final int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		
		if (!this.finalised) {
			if (this.currentDelegate == null) {
				if (this.curToken.equals(".end")) {
					this.finalised = true;
				} else if((char) token == ':') {
					this.curIndex = Short.parseShort(this.curToken);
					this.currentDelegate = new ReferenceToken();
				} else if(!Character.isWhitespace(token)) {
					this.curToken += (char) token;
				}
			} else {
				final boolean delegateFinished = this.currentDelegate.handle(token, offset);
				if(delegateFinished) {
					this.constants.add(this.curIndex - 1, currentDelegate);
					this.currentDelegate = null;
					this.curToken = "";
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}
}
