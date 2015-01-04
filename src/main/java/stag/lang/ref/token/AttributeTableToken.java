package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class AttributeTableToken implements Token {
	private final List<Token> attributes = new ArrayList<Token>();
	private boolean finalised = false;
	private Token currentDelegate = null;
	private String currentToken = "";

	@Override
	public boolean hasChildren() {
		return !this.attributes.isEmpty();
	}

	@Override
	public Collection<Token> getChildren() {
		return this.attributes;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		final ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort((short) this.attributes.size());

		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		
		if(this.currentDelegate != null) {
			final boolean delegateFinished = this.currentDelegate.handle(token, offset);
			
			if(delegateFinished) {
				this.attributes.add(currentDelegate);
				this.currentDelegate = null;
				this.currentToken = "";
			}
		} else {
			if(!Character.isWhitespace(token)) {				
				currentToken += (char) token;
				
				App.LOG.fine(currentToken);
			} else if(!this.currentToken.isEmpty()) {
				switch(currentToken) {
				case ".code":
					this.currentDelegate = new CodeAttributeToken();
					break;
				case ".end":
					this.finalised = true;
					break;
				default:
					throw new ParseException("No such method table element: " + this.currentToken, offset);
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}

}
