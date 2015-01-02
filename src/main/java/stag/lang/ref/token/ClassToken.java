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
	private ThisToken thisReference = new ThisToken();
	private Token superReference;
	
	private Token currentDelegate = null;
	private String currentToken = "";

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		return Arrays.asList(constantPool, thisReference);//, superReference);
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
	public boolean handle(int token) {
		App.LOG.entering(getClass().getName(), "handle");
		
		boolean ret = false;
		
		if(this.currentDelegate != null) {
			final boolean delegateFinished = this.currentDelegate.handle(token);
			
			if(delegateFinished) {
				this.currentDelegate = null;
			}
		} else {
			if(!Character.isWhitespace(token)) {
				currentToken += (char) token;
				
				switch(currentToken) {
				case ".this":
					this.currentDelegate = this.thisReference;
					this.currentToken = "";
					break;
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", ret);
		return ret;
	}

}
