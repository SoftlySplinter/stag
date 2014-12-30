package stag.lang.ref;

enum Keyword {
	// Types
	VOID("void"),
	INT("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double"),
	
	// Import definitions
	IMPORT("import"),
	
	// Field details
	CONSTANT("constant"),
	PRIVATE("private"),
	PROTECTED("protected"),
	PACKAGE("package"),
	THROWS("throws"),
	
	// Flow control
	IF("if"),
	ELSE("else"),
	SWITCH("switch"),
	FOR("for"),
	WHILE("while"),
	THROW("throw"),
	
	// Method flow control
	WHEN("when"),
	FORALL("forall"),
	EXISTS("exists");
	
	public final String keyword;
	private Keyword(final String keyword) {
		this.keyword = keyword;
	}
}
