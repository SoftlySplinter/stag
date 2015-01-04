package stag.lang.ref.token;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stag.lang.App;
import stag.lang.ref.Token;
import stag.lang.ref.Utils;

public class CodeToken implements Token {
	private String currentToken = "";
	private Map<Integer, Token> instructionSet = new HashMap<Integer, Token>();
	private boolean finalised;
	private Token currentDelegate;
	private String curToken;
	private int curIndex;

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Collection<Token> getChildren() {
		List<Token> tokens = new ArrayList<Token>();
		Integer[] lines = new Integer[this.instructionSet.size()];
		lines = this.instructionSet.keySet().toArray(lines);
		Arrays.sort(lines);
		for(Integer line: lines) {
			tokens.add(this.instructionSet.get(lines));
		}
		return tokens;
	}

	@Override
	public byte[] getBytes() throws ParseException {
		App.LOG.entering(getClass().getName(), "getBytes");
		final ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(this.getSize());
		
		App.LOG.exiting(getClass().getName(), "getBytes", Utils.str(buffer.array()));
		return buffer.array();
	}

	private int getSize() throws ParseException {
		int count = 0;
		for(Token t : this.getChildren()) {
			count += t.getBytes().length;
		}
		return count;
	}

	@Override
	public boolean handle(int token, int offset) throws ParseException {
		App.LOG.entering(getClass().getName(), "handle", (char) token);
		
		if (!this.finalised) {
			if (this.currentDelegate == null) {
				if (this.curToken.equals(".end")) {
					this.finalised = true;
				} else if((char) token == ':') {
					this.curIndex = Integer.parseInt(this.curToken);
					this.currentDelegate = new InstructionToken();
				} else if(!Character.isWhitespace(token)) {
					this.curToken += (char) token;
				}
			} else {
				final boolean delegateFinished = this.currentDelegate.handle(token, offset);
				if(delegateFinished) {
					this.instructionSet.put(this.curIndex, currentDelegate);
					this.currentDelegate = null;
					this.curToken = "";
				}
			}
		}
		
		App.LOG.exiting(getClass().getName(), "handle", this.finalised);
		return this.finalised;
	}

}
