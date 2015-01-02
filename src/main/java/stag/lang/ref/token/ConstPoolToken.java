package stag.lang.ref.token;

import java.text.ParseException;
import java.util.Collection;

import stag.lang.ref.Token;

public class ConstPoolToken implements Token {

	public int getClassReference(String className) {
		// TODO Auto-generated method stub
		return 0;
	}

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
		return new byte[0];
	}

	@Override
	public boolean handle(int token) {
		// TODO Auto-generated method stub
		return false;
	}

}
