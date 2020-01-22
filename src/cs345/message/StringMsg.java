/**
 * 
 */
package cs345.message;



/**
 * @author angel
 *
 */
public class StringMsg extends AbstractMsg implements Message {
	private String msg;
	
	
	public StringMsg(String msg){
		this.msg = msg;
	}
	
	// Get the strings of the msg objects passed in?
	@Override
	public String getString(Object... args) {				
		return msg;
	}

	
	@Override
	public String getAltString(int alt, Object... args) {		
		return msg;
	}

	
	

}
