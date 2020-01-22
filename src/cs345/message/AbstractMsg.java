package cs345.message;

/*
 * This is an abstract Message class that does messageOut and calls
 * print to message formatter and calls the getStrings for all 
 */

public abstract class AbstractMsg implements Message {
	
	@Override
	public void print(MessageFormatter out, Object... args) {
		out.print(getString(args));

	}

	
	@Override
	public void println(MessageFormatter out, Object... args) {
		out.println(getString(args));

	}

	
	@Override
	public void altPrint(int alt, MessageFormatter out, Object... args) {
		out.print(getAltString(alt, args));

	}

	
	@Override
	public void altPrintln(int alt, MessageFormatter out, Object... args) {
		out.println(getAltString(alt,args));

	}
	

}
