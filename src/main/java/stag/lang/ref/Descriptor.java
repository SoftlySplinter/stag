package stag.lang.ref;

public class Descriptor {
	public final String descriptor;
	public final int descriptorRef;
	
	public Descriptor(ConstantPool pool, String returnType, String... parameters) {
		this.descriptor = String.format("(%s)%s", stringify(parameters), stringify(returnType));
		this.descriptorRef = pool.put(Reference.string(this.descriptor));
	}

	private String stringify(String type) {
		if(type.endsWith("[]")) {
			return "[" + stringify(type.replace("[]", ""));
		}
		switch(type) {
		case "int":
			return "I";
		case "double":
			return "D";
		case "void":
			return "V";
		default:
			return String.format("L%s;", type);
		}
	}

	private String stringify(String[] parameters) {
		String ret = "";
		for(final String parameter: parameters) {
			ret += stringify(parameter);
		}
		return ret;
	}
}
