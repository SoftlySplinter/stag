package stag.lang.ref;

import java.util.HashMap;

import stag.lang.App;

public class ReferenceList extends HashMap<Integer, Reference> {
	private static final long serialVersionUID = 2747987781303471944L;

	public int put(Reference r) {
		App.LOG.entering(getClass().getName(), "put", r);
		int ref = this.size() + 1;
		this.put(ref, r);
		
		App.LOG.exiting(getClass().getName(), "put", ref);
		return ref;
	}
}
