package stag.lang.ref;

public enum Type {
	String(1, -1),
	Int(3, 2),
	Float(4, 2),
	Long(5, 4),
	Double(6, 4),
	ClassRef(7, 2),
	StringRef(8, 2),
	FieldRef(9, 4),
	MethodRef(10, 4),
	InterfaceMethodRef(11, 4),
	Descriptor(12, 4);
	
	public final int tag;
	public final int bytes;
	private Type(final int tag, final int bytes) {
		this.tag = tag;
		this.bytes = bytes;
	}
}
