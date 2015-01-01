package stag.lang.ref.attr;

import static stag.lang.ref.Utils.u2;
import static stag.lang.ref.Utils.u4;

import java.nio.ByteBuffer;

import stag.lang.App;
import stag.lang.ref.Attribute;
import stag.lang.ref.ConstantPool;
import stag.lang.ref.Utils;

public class CodeAttribute extends Attribute {
	private static int getAttributeCount(Attribute[] attributes) {
		int count = 0;
		for(final Attribute attr: attributes) {
			count += attr.attributeLength;
		}
		return count;
	}
	
	public final int maxStack;
	public final int maxLocals;
	public final byte[] code;
	public final byte[] execptionTable;
	public final Attribute[] attributes;
	
	public CodeAttribute(int maxStack, int maxLocals, byte[] code, byte[] exceptionTable, Attribute[] attributes, ConstantPool pool) {
		super("Code", 
				2 + 2 + 4 + code.length + 2 + exceptionTable.length * (2 + 2 + 2 + 2) + 2 + getAttributeCount(attributes), 
				pool);
		App.LOG.entering(this.getClass().getName(), "<init>", String.format("%d %d (%s) (%s) (%s)", maxStack, maxLocals, Utils.str(code), Utils.str(exceptionTable), attributes));
		
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		this.code = code;
		this.execptionTable = exceptionTable;
		this.attributes = attributes;
		
		App.LOG.exiting(this.getClass().getName(), "<init>");
	}
	
	@Override
	public byte[] getBytes() {
		App.LOG.entering(this.getClass().getName(), "getBytes");	
		ByteBuffer buffer = ByteBuffer.allocate(this.attributeLength + 6);
		
		buffer.put(u2(this.nameIndex));
		buffer.put(u4(this.attributeLength));
		buffer.put(u2(this.maxStack));
		buffer.put(u2(this.maxLocals));
		buffer.put(u4(this.code.length));
		buffer.put(code);
		buffer.put(u2(this.execptionTable.length));
		buffer.put(execptionTable);
		buffer.put(u2(this.attributes.length));
		for(final Attribute attribute: this.attributes) {
			buffer.put(attribute.getBytes());
		}
		App.LOG.exiting(this.getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}
}
