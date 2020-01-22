package cs345.game;

import java.util.ArrayList;
import java.util.List;

public class Terms implements Term {
	private List<Word> words = new ArrayList<Word>();
	
	// Constructor
		public Terms(){
			
		}

	@Override
	public void addWord(Word word) {
		words.add(word);

	}

	@Override
	public boolean contains(Word w) {
		return words.contains(w);
	}

	/**
	 * @return the words
	 */
	public List<Word> getWords() {
		return words;
	}

	/**
	 * @param words the words to set
	 */
	public void setWords(List<Word> words) {
		this.words = words;
	}

}
