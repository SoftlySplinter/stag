package stag.lang.ref;

import java.text.ParseException;
import java.util.Collection;

public interface Token {
	public boolean hasChildren();
	public Collection<Token> getChildren();
	public byte[] getBytes() throws ParseException;
	public Token handle(String sval) throws ParseException;
	public Token handle(double nval) throws ParseException;
}
