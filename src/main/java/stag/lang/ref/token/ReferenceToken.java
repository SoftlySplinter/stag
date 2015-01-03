package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Collection;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class ReferenceToken implements Token {
	private String type = "";
	private String strValue = "";
	private String strValue2 = "";
	private boolean atValue = false;
	private boolean finalised = false;
	private boolean quote = false;
	private boolean value1Finalised = false;

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
		final ByteBuffer buffer = ByteBuffer.allocate(this.getSize() + 1);
		
		switch(this.type) {
		case "utf8":
			buffer.put((byte) 1);
			buffer.putShort((short) this.strValue.getBytes().length);
			buffer.put(this.strValue.getBytes());
			break;
		case "int":
			buffer.put((byte) 3);
			buffer.putInt(Integer.parseInt(this.strValue));
			break;
		case "float":
			buffer.put((byte) 4);
			buffer.putFloat(Float.parseFloat(this.strValue));
			break;
		case "long":
			buffer.put((byte) 5);
			buffer.putLong(Long.parseLong(this.strValue));
			break;
		case "double":
			buffer.put((byte) 6);
			buffer.putDouble(Double.parseDouble(this.strValue));
			break;
		case "class":
			buffer.put((byte) 7);
			buffer.putShort(Short.parseShort(this.strValue));
			break;
		case "string":
			buffer.put((byte) 8);
			buffer.putShort(Short.parseShort(this.strValue));
			break;
		case "field":
			buffer.put((byte) 9);
			buffer.putShort(Short.parseShort(this.strValue));
			buffer.putShort(Short.parseShort(this.strValue2));
			break;
		case "method":
			buffer.put((byte) 10);
			buffer.putShort(Short.parseShort(this.strValue));
			buffer.putShort(Short.parseShort(this.strValue2));
			break;
		case "interface":
			buffer.put((byte) 11);
			buffer.putShort(Short.parseShort(this.strValue));
			buffer.putShort(Short.parseShort(this.strValue2));
			break;
		case "descriptor":
			buffer.put((byte) 12);
			buffer.putShort(Short.parseShort(this.strValue));
			buffer.putShort(Short.parseShort(this.strValue2));
			break;
		}
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	private int getSize() throws ParseException {
		switch(this.type) {
		case "utf8":
			return 2 + this.strValue.getBytes().length;
		case "int":
		case "float":
			return 4;
		case "long":
		case "double":
			return 8;
		case "class":
		case "string":
			return 2;
		case "field":
		case "method":
		case "interface":
		case "descriptor":
			return 4;
		default:
			throw new ParseException("Unknown type: " + this.type, -1);
		}
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		
		App.LOG.fine(String.format("type: %s value: %s", this.type, this.strValue));
		
		if(!this.finalised) {
			if(!atValue) {
				if(Character.isWhitespace((char) token)) {
					atValue = !this.type.isEmpty();
				} else {
					this.type += (char) token;
				}
			} else {
				switch(this.type){
				case "utf8": 
					this.finalised = parseStringValue((char) token);
					break;
				case "field":
				case "method":
				case "interface":
				case "descriptor":
					this.finalised = parseReference((char) token);
					break;
				default:
					this.finalised = parsePrimativeValue((char) token);
				}
			}
		}

		App.LOG.entering(getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}

	private boolean parseReference(char token) {
		if (Character.isWhitespace(token)) {
			if(!this.strValue2.isEmpty()) {
				return true;
			} else if(!this.strValue.isEmpty()) {
				this.value1Finalised = true;
			}
			return false;
		} else {
			if(!this.value1Finalised) {
				this.strValue += token;
			} else {
				this.strValue2 += token;
			}
			return false;
		}
	}

	private boolean parseStringValue(char token) {
		if(token == '"') {
			if(this.quote) {
				return true;
			} else {
				this.quote = true;
			}
		} else {
			this.strValue += token;
		}
		return false;
	}

	private boolean parsePrimativeValue(char token) {
		if (strValue.isEmpty() && Character.isWhitespace(token)) {
			return false;
		} else {
			if (Character.isWhitespace(token)) {
				return true;
			} else {
				this.strValue += token;
				return false;
			}
		}
	}

}
