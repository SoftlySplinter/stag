package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class CodeAttributeToken implements Token {
	private String currentToken = "";
	private Token currentDelegate;
	private boolean finalised;
	
	private Token name = new ConstantReferenceToken();
	private Token stack = new ConstantReferenceToken();
	private Token locals = new ConstantReferenceToken();
	private Token code = new CodeToken();
	private Token exceptions = new ExceptionTableToken();
	private Token attributes = new AttributeTableToken();

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList(this.stack, this.locals, this.code, this.exceptions, this.attributes); //TODO
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		final ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.put(this.name.getBytes());
		buffer.putInt(this.stack.getBytes().length + this.locals.getBytes().length + this.code.getBytes().length + this.exceptions.getBytes().length + this.attributes.getBytes().length);

		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
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
				case ".name":
					this.currentDelegate = this.name;
					break;
				case ".stack":
					this.currentDelegate = this.stack;
					break;
				case ".locals":
					this.currentDelegate = this.locals;
					break;
				case ".code":
					this.currentDelegate = this.code;
					break;
				case ".exceptions":
					this.currentDelegate = this.exceptions;
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
