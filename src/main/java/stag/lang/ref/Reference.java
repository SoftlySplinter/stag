package stag.lang.ref;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import stag.lang.App;

public class Reference {
	public final Type type;
	public final byte[] value;
	
	public Reference(Type type, byte[] value) {
		App.LOG.entering(getClass().getName(), "<init>");
		
		this.type = type;
		this.value = value;
		
		App.LOG.exiting(getClass().getName(), "<init>");
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s): %s%s", this.type.toString(), this.stringValue(), Utils.str(Utils.u2(this.type.tag)), Utils.str(this.value));
	}

	private String stringValue() {
		switch(this.type) {
		case String:
			byte[] strB = new byte[this.value.length - 2];
			for(int i = 2; i < this.value.length; i++) {
				strB[i - 2] = this.value[i];
			}
			return new String(strB, Charset.defaultCharset());
		case StringRef:
		case ClassRef:
		case FieldRef:
		case MethodRef:
		case InterfaceMethodRef:
			return String.valueOf(Utils.b2s(this.value));
		case Int:
			return String.valueOf(Utils.b2i(this.value));
		default:
			return "???";
		}
		
	}

	public static Reference string(final String value) {
		App.LOG.entering(Reference.class.getName(), "string", value);
		final byte[] bytes = value.getBytes();
		
		final ByteBuffer buf = ByteBuffer.allocate(2 + bytes.length);
		buf.put(Utils.u2(bytes.length));
		buf.put(bytes);
		
		final Reference ref = new Reference(Type.String, buf.array());
		App.LOG.exiting(Reference.class.getName(), "string", ref);
		return ref;
	}

	public static Reference classRef(final int classNameReference) {
		App.LOG.entering(Reference.class.getName(), "classRef", classNameReference);
		final Type type = Type.ClassRef;
		
		final ByteBuffer buffer = ByteBuffer.allocate(type.bytes);
		buffer.put(Utils.u2(classNameReference));
		
		final Reference ref = new Reference(type, buffer.array());
		App.LOG.exiting(Reference.class.getName(), "string", ref);
		return ref;
	}
}
