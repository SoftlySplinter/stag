package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class AccessFlagToken implements Token {
	private String flags = "";
	private boolean hasNext = true;
	private boolean finalised = false;
	
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
		App.LOG.entering(getClass().getName(), "getBytes");
		ByteBuffer buffer = ByteBuffer.allocate(2);
		
		short flags = 0x0000;
		
		for(String flag: this.flags.split(",")) {
			switch(flag) {
			case "public":
				flags |= 0x0001;
				break;
			case "private":
				flags |= 0x0002;
				break;
			case "protected":
				flags |= 0x0004;
				break;
			case "static":
				flags |= 0x0008;
				break;
			case "final":
				flags |= 0x0010;
				break;
			case "super":
			case "synchronized":
				flags |= 0x0020;
				break;
			case "volatile":
			case "bridge":
				flags |= 0x0040;
				break;
			case "transient":
			case "vaargs":
				flags |= 0x0080;
				break;
			case "native":
				flags |= 0x0100;
				break;
			case "interface":
				flags |= 0x0200;
				break;
			case "abstract":
				flags |= 0x0400;
				break;
			case "strict":
				flags |= 0x0800;
				break;
			case "synthetic":
				flags |= 0x1000;
				break;
			case "annotation":
				flags |= 0x2000;
				break;
			case "enum":
				flags |= 0x4000;
				break;
			default:
				throw new ParseException("No such access flag " + flag, -1);
			}
		}
		
		buffer.putShort(flags);
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		if(Character.isWhitespace((char) token)) {
			if(!this.hasNext) {
				this.finalised = true;
			}
		} else {
			this.hasNext = (char) token == ',';
			this.flags += (char) token;
		}
		
		App.LOG.exiting(getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}

}
