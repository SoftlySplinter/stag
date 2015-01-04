package stag.lang.ref.token;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;

public class MethodInfoToken implements Token {
	private boolean finalised = false;
	private String currentToken = "";
	private Token currentDelegate = null;
	
	private Token accessFlags = new AccessFlagToken();
	private Token name = new ConstantReferenceToken();
	private Token descriptor = new ConstantReferenceToken();
	private Token attributes = new AttributeTableToken();

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList(this.accessFlags, this.name, this.descriptor, this.attributes);
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		App.LOG.exiting(getClass().getName(), "getBytes");
		return new byte[0];
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		
		if(this.currentDelegate != null) {
			final boolean delegateFinished = this.currentDelegate.handle(token, offset);
			
			if(delegateFinished) {
				this.currentDelegate = null;
				this.currentToken = "";
			}
		} else {
			if(!Character.isWhitespace(token)) {				
				currentToken += (char) token;
				
				App.LOG.fine(currentToken);
			} else if(!this.currentToken.isEmpty()) {
				switch(currentToken) {
				case ".access":
					this.currentDelegate = this.accessFlags;
					break;
				case ".name":
					this.currentDelegate = this.name;
					break;
				case ".descriptor":
					this.currentDelegate = this.descriptor;
					break;
				case ".attributes":
					this.currentDelegate = this.attributes;
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
