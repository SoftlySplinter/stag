package stag.lang.comp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import stag.lang.App;
import stag.lang.ref.Interface;
import stag.lang.ref.Lexer;
import stag.lang.ref.Method;
import stag.lang.ref.Reference;
import stag.lang.ref.Utils;

/**
 * A compiler for the Stag language.
 * <p>
 * Compiles Stag source code into a Java class format which can be run directly
 * using the JVM.
 * 
 * @author Alexander Brown
 * @version 1.0-SNAPSHOT
 */
public class Compiler {
	public final Lexer lexer;

	private Compiler(Lexer lexer) {
		App.LOG.entering(getClass().getName(), "<init>", lexer);

		this.lexer = lexer;

		App.LOG.exiting(getClass().getName(), "<init>");
	}

	private final void compile() throws IOException {
		App.LOG.entering(getClass().getName(), "compile");

		// TODO Consider arguments, etc.
		final String classFileName = this.lexer.source.replace(".stag",
				".class");
		App.LOG.finest(String.format(
				"Resolved class file for source '%s' as '%s'",
				this.lexer.source, classFileName));

		final File classFile = new File(classFileName);
		App.LOG.finest(String.format("Opening file '%s' for writing",
				classFile.getAbsoluteFile()));

		try (OutputStream out = Files.newOutputStream(classFile.toPath(),
				StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			App.LOG.fine("Writing magic number");
			out.write(0xCA);
			out.write(0xFE);
			out.write(0xBA);
			out.write(0xBE);

			App.LOG.fine("Writing version information (J2SE 1.7)");
			out.write(0x00);
			out.write(0x00);
			out.write(0x00);
			out.write(0x33);

			App.LOG.fine("Writing constant pool");
			out.write(Utils.u2(this.lexer.constPool.size() + 1));
			for (Reference reference : this.lexer.constPool.values()) {
				App.LOG.fine("Writing constant: " + reference);
				out.write((byte) reference.type.tag);
				out.write(reference.value);
			}

			App.LOG.fine("Writing access flags");
			out.write(Utils.u2(0x0001));

			App.LOG.fine("Writing this reference");
			out.write(Utils.u2(this.lexer.getThisRef()));

			App.LOG.fine("Writing super reference");
			out.write(Utils.u2(this.lexer.getSuperRef()));

			App.LOG.fine("Writing interface count");
			out.write(Utils.u2(this.lexer.interfaces.size()));
			
			App.LOG.fine("Writing interface list");
			for(final Interface iface: this.lexer.interfaces) {
				App.LOG.fine("Writing interface: " + iface);
				out.write(Utils.u2(iface.reference));
			}

			App.LOG.fine("Writing field count");
			out.write(Utils.u2(0));
			// TODO Write fields

			App.LOG.fine("Writing method count");
			out.write(Utils.u2(this.lexer.methods.size()));
			
			App.LOG.fine("Writing method list");
			for(final Method method: this.lexer.methods) {
				out.write(method.getBytes());
			}

			App.LOG.fine("Writing attribute count");
			out.write(Utils.u2(0));
			// TODO Write attributes
		}

		try (InputStream in = Files.newInputStream(classFile.toPath(),
				StandardOpenOption.READ)) {
			String contents = "";
			for (int b = in.read(); b != -1; b = in.read()) {
				contents += String.format("%02x ", b);
			}
			App.LOG.finest(contents);
		}

		App.LOG.exiting(getClass().getName(), "compile");
	}

	public static final void compiler(Lexer lexer) throws IOException {
		App.LOG.entering(Compiler.class.getName(), "compiler", lexer);

		Compiler comp = new Compiler(lexer);
		comp.compile();

		App.LOG.exiting(Compiler.class.getName(), "compiler");
	}
}
