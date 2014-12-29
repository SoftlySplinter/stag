package stag.lang.ref;

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
}
