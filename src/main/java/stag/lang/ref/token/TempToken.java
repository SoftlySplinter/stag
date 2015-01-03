package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Collection;

import stag.lang.ref.Token;

public class TempToken implements Token {
	private String cur = "";
	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Collection<Token> getChildren() {
		return null;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		return ByteBuffer.allocate(2).putShort((short) 0).array();
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		cur += (char) token;
		return cur.endsWith(".end");
	}

}
