package stag.lang.ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.attr.CodeAttribute;
/**
 * Lexical Analyser for the Stag programming language.
 * 
 * @author Alexander D Brown
 * @version 1.0-SNAPSHOT
 */
public class Lexer {
	/** The references in a source file */
	public final ConstantPool constPool;

	public final List<Interface> interfaces;
	public final List<Method> methods;
	
	public final String source;
	
	private int thisRef = -1;
	private int superRef = -1;

	
	private Lexer(String source) {
		App.LOG.entering(getClass().getName(), "<init>", source);
		
		this.constPool = new ConstantPool();
		this.interfaces = new ArrayList<Interface>();
		this.methods = new ArrayList<Method>();
		this.source = source;
		
		App.LOG.exiting(getClass().getName(), "<init>");
	}
	
	private final void parse() throws IOException {
		App.LOG.entering(getClass().getName(), "parse");
		
		final String className = source.replace(".stag", "");
		App.LOG.fine(String.format("Resolved class name as '%s'", className));
		
		final File sourceFile = new File(source);
		App.LOG.finest("Opening UTF-8 file " + sourceFile.getAbsolutePath());
		
		try(BufferedReader reader = Files.newBufferedReader(sourceFile.toPath(), Charset.forName("UTF-8"))) {
			// Set up the references for this.
			int classNameReference = this.constPool.put(Reference.string(className));
			thisRef = this.constPool.put(Reference.classRef(classNameReference));
			
			// Parse the file
			parse(reader);
			
			// Superclass is object if one was not found during parsing.
			if(this.superRef == -1) {
				int objectNameReference = this.constPool.put(Reference.string("java/lang/Object"));
				superRef = this.constPool.put(Reference.classRef(objectNameReference));
			}
		}
		
		App.LOG.exiting(getClass().getName(), "parse");
	}

	private final void parse(BufferedReader reader) throws IOException {
		App.LOG.entering(getClass().getName(), "parse", reader);
		
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {
			App.LOG.finest("Read line: " + line);
		}

		App.LOG.config("[test] Adding interface " + Serializable.class);
		this.interfaces.add(new Interface("java/io/Serializable", constPool));
		
		App.LOG.config("[test] Adding main method");
		Method main = new Method(Method.PUBLIC, "main", new Descriptor(this.constPool, "void", "java/lang/String[]"), this.constPool);
		Attribute attr = new CodeAttribute(0, 0, new byte[0], new byte[0], new Attribute[0], constPool);
		main.attributes.add(attr);
		this.methods.add(main);

		App.LOG.exiting(getClass().getName(), "parse");
	}

	public static Lexer analyse(String sourceFile) throws IOException {
		App.LOG.entering(Lexer.class.getName(), "analyse", sourceFile);
		
		Lexer lexer = new Lexer(sourceFile);
		lexer.parse();
		
		App.LOG.exiting(Lexer.class.getName(), "analyse", lexer);
		return lexer;
	}
	
	public int getSuperRef() {
		return superRef;
	}
	
	public int getThisRef() {
		return thisRef;
	}
}