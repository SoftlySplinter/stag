package stag.lang.ref;

import stag.lang.App;

public abstract class Attribute {
	public final String name;
	public final int nameIndex;
	public final int attributeLength;
	public final byte[] info;
	
	public Attribute(final String name, final int attributeLength, final ConstantPool pool) {
		App.LOG.entering(this.getClass().getName(), "<init>", String.format("%s %d", name, attributeLength));
		this.name = name;
		this.nameIndex = pool.put(Reference.string(this.name));
		this.attributeLength = attributeLength;
		this.info = new byte[this.attributeLength];
		App.LOG.exiting(this.getClass().getName(), "<init>");
	}

	public abstract byte[] getBytes();
}
