package stag.lang.ref;

import static stag.lang.ref.Utils.u2;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import stag.lang.App;

public class Method {
	public static final int PUBLIC =       0x0001;
	public static final int PRIVATE =      0x0002;
	public static final int PROTECTED =    0x0004;
	public static final int STATIC =       0x0008;
	public static final int FINAL =        0x0010;
	public static final int SYNCHRONIZED = 0x0020;
	public static final int BRIDGE =       0x0040;
	public static final int VAARGS =       0x0080;
	public static final int NATIVE =       0x0100;
	public static final int ABSTRACT =     0x0200;
	public static final int STRICT =       0x8000;
	public static final int SYNTHETIC =    0x1000;

	public final int accessFlags;
	public final String name;
	public final int nameRef;
	public final Descriptor descriptor;
	public final List<Attribute> attributes;
	
	public Method(int flags, String name, Descriptor descriptor, ConstantPool constPool) {
		App.LOG.entering(this.getClass().getName(), "<init>", String.format("%d %s %s", flags, name, descriptor));
		this.accessFlags = flags;
		this.name = name;
		this.nameRef = constPool.put(Reference.string(name));
		this.descriptor = descriptor;
		this.attributes = new ArrayList<Attribute>();
		App.LOG.exiting(this.getClass().getName(), "<init>");
	}

	public byte[] getBytes() {
		App.LOG.entering(this.getClass().getName(), "getBytes");
		final ByteBuffer buffer = ByteBuffer.allocate(getLength());
		buffer.put(u2(this.accessFlags));
		buffer.put(u2(this.nameRef));
		buffer.put(u2(this.descriptor.descriptorRef));
		buffer.put(u2(this.attributes.size()));
		buffer.put(getAttributeBytes());
		
		App.LOG.entering(this.getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	public int getLength() {
		return 2 + 2 + 2 + 2 + getAttributeCount();
	}

	private int getAttributeCount() {
		int count = 0;
		for(final Attribute attr: this.attributes) {
			count += attr.attributeLength + 6;
		}
		return count;
	}

	private ByteBuffer getAttributeBytes() {
		App.LOG.entering(this.getClass().getName(), "getAttributeBytes");
		ByteBuffer buffer = ByteBuffer.allocate(getAttributeCount());
		for(final Attribute attr: this.attributes) {
			buffer.put(attr.getBytes());
		}
		App.LOG.exiting(this.getClass().getName(), "getAttributeBytes", Utils.str(buffer.array()));
		return buffer;
	}
}
