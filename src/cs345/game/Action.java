package cs345.game;

public class Action {
	  private Term term1, term2;
      private ActionMethod action;
      private int priority;
      private ValidMethod valid;
      
      
      
	/**
	 * @return the action
	 */
	public void doAction(Game game, Word w1, Word w2) {
		action.doAction(game, w1, w2);
		
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(ActionMethod action) {
		this.action = action;
	}
	/**
	 * @return the term1
	 */
	public Term getTerm1() {
		return term1;
	}
	/**
	 * @param term1 the term1 to set
	 */
	public void setTerm1(Term term1) {
		this.term1 = term1;
	}
	/**
	 * @return the term2
	 */
	public Term getTerm2() {
		return term2;
	}
	/**
	 * @param term2 the term2 to set
	 */
	public void setTerm2(Term term2) {
		this.term2 = term2;
	}
	public void setPriority(int priority) {
		this.priority = priority;
		
	}
	public void setValid(ValidMethod valid) {
		this.valid = valid;
		
	}
	public int getPriority(){
		return this.priority;
	}
	
	public ValidMethod getValid(){
		return this.valid;
	}
	
	public boolean checkValid(Game g, Word w1, Word w2) {
	        return (this.valid) == null || 
	        		this.valid.isValid(g, w1, w2);
	 }
	

}