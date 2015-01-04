package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class ExceptionTableToken implements Token {
	private String currentToken = "";
	private List<Token> exceptions = new ArrayList<Token>();
	
	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		return this.exceptions;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		final ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort((short) exceptions.size());
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		this.currentToken += (char) token;
		return this.currentToken.endsWith(".end");
	}

}
