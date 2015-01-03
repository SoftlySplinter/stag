package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Collection;

import stag.lang.ref.Token;

public class TempToken implements Token {

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Token> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		return ByteBuffer.allocate(2).putShort((short) 0).array();
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		// TODO Auto-generated method stub
		return false;
	}

}
