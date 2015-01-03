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
	private ThisToken thisReference = new ThisToken();
	private Token superReference = new ThisToken();
	private Token interfaceTable = new TempToken();
	private Token fieldTable = new TempToken();
	private Token methodTable = new TempToken();
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
				this.currentToken = ("" + (char) token).trim();
			}
		} else {
			if(!Character.isWhitespace(token)) {				
				currentToken += (char) token;
				
				App.LOG.fine(currentToken);
				
				switch(currentToken) {
				case ".constants":
					this.currentDelegate = this.constantPool;
					break;
				case ".access":
					this.currentDelegate = this.accessFlags;
					break;
				case ".this":
					this.currentDelegate = this.thisReference;
					break;
				case ".super":
					this.currentDelegate = this.superReference;
					break;
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", ret);
		return ret;
	}

}
