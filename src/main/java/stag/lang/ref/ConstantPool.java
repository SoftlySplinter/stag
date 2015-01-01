package stag.lang.ref;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;

import stag.lang.App;

public class ConstantPool extends HashMap<Integer, Reference> {
	private static final long serialVersionUID = 2747987781303471944L;

	public int put(Reference r) {
		App.LOG.entering(getClass().getName(), "put", r);
		int ref = this.size() + 1;
		this.put(ref, r);
		
		App.LOG.exiting(getClass().getName(), "put", ref);
		return ref;
	}

	public int containsClass(String className) {
		App.LOG.entering(getClass().getName(), "containsClass", className);
		
		int index = -1;
		
		for(Entry<Integer, Reference> entry : this.entrySet()) {
			final Reference ref = entry.getValue();
			if(ref.type == Type.ClassRef) {
				App.LOG.finest("Found class reference: " + ref);
				int classNameRef = Utils.b2s(ref.value);

				App.LOG.finest(String.format("Checking for string reference %d", classNameRef));
				Reference stringRef = this.get(classNameRef);

				App.LOG.finest("Found string reference: " + stringRef);
				String classRefValue = new String(stringRef.value, Charset.defaultCharset());
				
				App.LOG.finest("String reference contains class name " + classRefValue);
				if(classRefValue.equals(className)) {
					App.LOG.finest("Found class reference for " + className + " at index " + entry.getKey());
					index = entry.getKey();
					break;
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "containsClass", index);
		return index;
	}
}
