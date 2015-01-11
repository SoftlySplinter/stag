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
	
	private ConstPoolToken constantPool = new ConstPoolToken(this);
	private Token accessFlags = new AccessFlagToken(this);
	private Token thisReference = null;
	private Token superReference = null;
	private Token interfaceTable = null;
	private Token fieldTable = null;
	private Token methodTable = null;
	private Token attributeTable = null;
	
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
	public Token handle(double nval) throws ParseException {
		return this;
	}
	
	@Override
	public Token handle(String sval) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", sval);
		Token next = this;
		switch(sval) {
		case "constants":
			next = this.constantPool;
			break;
		case "access":
			next = this.accessFlags;
			break;
		case "this":
			next = this.thisReference;
			break;
		case "super":
			next = this.superReference;
			break;
		case "interfaces":
			next = this.interfaceTable;
			break;
		case "fields":
			next = this.fieldTable;
			break;
		case "methods":
			next = this.methodTable;
			break;
		case "attributes":
			next = this.attributeTable;
			break;
		default:
			ParseException t = new ParseException("Illegal token: " + sval, -1);
			App.LOG.throwing(getClass().getName(), "handle", t);
			throw t;
		}
		
		App.LOG.exiting(getClass().getName(), "handle", next);
		return next;
	}
}
