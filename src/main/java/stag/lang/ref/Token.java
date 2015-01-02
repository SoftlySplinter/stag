package stag.lang.ref;

import java.text.ParseException;
import java.util.Collection;

public interface Token {
	public boolean hasChildren();
	public Collection<Token> getChildren();
	public byte[] getBytes() throws ParseException;
	public boolean handle(int token);
}
