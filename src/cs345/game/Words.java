package cs345.game;

import java.util.ArrayList;
import java.util.List;

public class Words implements Word {
	private String word;
    private MatchType match;
    private List<String> wordAbb = new ArrayList<String>(); 
   
    /**
     * Constructor
     */
    public Words(){
    	//super();    	    	
    }
    
public Words(String words, MatchType match) {
	super();	
	this.word = words;
	this.match = match;
		
	}

public MatchType match(String s){
	if(getWord().startsWith(s.toLowerCase())){
		return getMatch();
	}
	return MatchType.NONE;		
	}	

/* 
 * returns word object toString
 */
public String toString(){
	return word.toString();
}
/**
 * @return the word
 */
public String getWord() {
	return word.toLowerCase();
}

/**
 * @param word the word to set
 */
public void setWord(String word) {
	this.word = word;
}

/**
 * @return the match
 */
public MatchType getMatch() {
	return match;
}

/**
 * @param match the match to set
 */
public void setMatch(MatchType match) {
	this.match = match;
}

@Override
public void addAbbreviation(String string) {
	this.wordAbb.add(string);
	
}
public List<String> getwordAbbs(){
	return this.wordAbb;
}

	
	
	

}
