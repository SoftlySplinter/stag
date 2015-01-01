package stag.lang.ref;

import stag.lang.App;

public class Interface {
	public final int reference;
	private final String interfaceName;
	
	public Interface(String interfaceName, ConstantPool constPool) {
		App.LOG.entering(getClass().getName(), "<init>");
		this.interfaceName = interfaceName;
		
		final int classRef = constPool.containsClass(interfaceName);
		if(classRef == -1) {
			App.LOG.fine(String.format("No reference for interface %s.", interfaceName));
			final int classNameRef = constPool.put(Reference.string(interfaceName));
			this.reference = constPool.put(Reference.classRef(classNameRef));
		} else {
			this.reference = classRef;
		}
		
		App.LOG.exiting(getClass().getName(), "<init>");
	}
	
	@Override
	public String toString() {
		return String.format("Interface '%s': %s", this.interfaceName, Utils.str(Utils.u2(this.reference)));
	}
}
