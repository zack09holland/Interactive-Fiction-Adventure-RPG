package cs345.message;

/*
 * Get message at index
 */
public class ArgMsg extends AbstractMsg implements Message {
	private int index;
	
	public ArgMsg(int index){
		this.index = index;
		
	}
	
	

	@Override
	public String getString(Object... args) {
		
		if(!(index > args.length || index < 0)){
			return args[index].toString();
		}
		throw new IndexOutOfBoundsException();
	}

	
		
	
	
	@Override 
	public String getAltString(int alt, Object... args) {
		if(!(index > args.length || index < 0)){
			return (args[index].toString());
		}
		throw new IndexOutOfBoundsException();
	}
		
			

	
	

}
