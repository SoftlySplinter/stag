package stag.lang.ref;

import java.nio.ByteBuffer;

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
		String ret = String.format("%s: ", this.type.toString());
		for(byte b : u2(this.type.tag)) {
			ret += String.format("0x%02x ", b);
		}
		
		for(byte b : this.value) {
			ret += String.format("0x%02x ", b);
		}
		
		return ret;
	}

	public static Reference string(final String value) {
		App.LOG.entering(Reference.class.getName(), "string", value);
		final byte[] bytes = value.getBytes();
		
		final ByteBuffer buf = ByteBuffer.allocate(2 + bytes.length);
		buf.put(u2(bytes.length));
		buf.put(bytes);
		
		final Reference ref = new Reference(Type.String, buf.array());
		App.LOG.exiting(Reference.class.getName(), "string", ref);
		return ref;
	}

	public static Reference classRef(final int classNameReference) {
		App.LOG.entering(Reference.class.getName(), "classRef", classNameReference);
		final Type type = Type.ClassRef;
		
		final ByteBuffer buffer = ByteBuffer.allocate(type.bytes);
		buffer.put(u2(classNameReference));
		
		final Reference ref = new Reference(type, buffer.array());
		App.LOG.exiting(Reference.class.getName(), "string", ref);
		return ref;
	}
	
	public static byte[] u2(int value) {
		return ByteBuffer.allocate(2).putShort((short) value).array();
	}
}
