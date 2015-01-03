package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class ClassToken implements Token {
	public static final short MAJOR_VERSION = 0x0033;
	public static final short MINOR_VERSION = 0x0000;
	
	private ConstPoolToken constantPool = new ConstPoolToken();
	private Token accessFlags = new AccessFlagToken();
	private ConstantReferenceToken thisReference = new ConstantReferenceToken();
	private Token superReference = new ConstantReferenceToken();
	private Token interfaceTable = new TempToken();
	private Token fieldTable = new TempToken();
	private Token methodTable = new MethodTableToken();
	private Token attributeTable = new TempToken();
	
	private Token currentDelegate = null;
	private String currentToken = "";

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList(constantPool, accessFlags, thisReference, superReference, interfaceTable, fieldTable, methodTable, attributeTable);
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		
		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putShort(MINOR_VERSION);
		buffer.putShort(MAJOR_VERSION);
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	@Override
	public boolean handle(final int token, final int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		
		boolean ret = false;
		
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
				case ".constants":
					setDelegate(this.constantPool, offset);
					break;
				case ".access":
					setDelegate(this.accessFlags, offset);
					break;
				case ".this":
					this.setDelegate(this.thisReference, offset);
					break;
				case ".super":
					this.setDelegate(this.superReference, offset);
					break;
				case ".interfaces":
					this.setDelegate(this.interfaceTable, offset);
					break;
				case ".fields":
					this.setDelegate(this.fieldTable, offset);
					break;
				case ".methods":
					this.setDelegate(this.methodTable, offset);
					break;
				case ".attributes":
					this.setDelegate(this.attributeTable, offset);
					break;
				default:
					throw new ParseException("No such class element: " + this.currentToken, offset);
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", ret);
		return ret;
	}

	private void setDelegate(Token delegate, int offset) throws ParseException {
		if(this.currentDelegate != null) {
			throw new ParseException("Delegate already exists", offset);
		}
		this.currentDelegate = delegate;
	}

}
