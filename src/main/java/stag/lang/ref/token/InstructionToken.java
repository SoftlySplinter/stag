package stag.lang.ref.token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import stag.lang.App;
import stag.lang.ref.Token;

public class InstructionToken implements Token {
	private boolean finalised = false;
	private String instruction = "";
	private List<String> arguments = new ArrayList<String>();
	private int argLength = -1;
	private int currentArg = -1;

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Collection<Token> getChildren() {
		return null;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		return null;
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle");
		
		if(!this.finalised) {
			if(!Character.isWhitespace(offset)) {
				if(this.argLength < 0) {
					this.instruction += (char) offset;
				} else {
					if(this.arguments.size() < this.currentArg) {
						this.arguments.add(this.currentArg, "");
					}
					
					final String arg = this.arguments.get(this.currentArg) + (char) offset;
					this.arguments.add(this.currentArg, arg);
				}
			} else {
				if(!this.instruction.isEmpty() && this.argLength < 0) {
					this.argLength = getArgLength(this.instruction);
					this.currentArg++;
				}
				
				this.finalised = this.arguments.size() == this.argLength;
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}

	private int getArgLength(String instruction) {
		switch(instruction) {
		case "astore_1":
		case "aload_1":
		case "return":
			return 0;
		case "ldc":
		case "getstatic":
		case "invokevirtual":
			return 1;
		default:
			return 0;
		}
	}

}
