
public class Content {
	private String paragraph;
	
	public Content(String p) {
		paragraph = p;
	}
	
	public int getCount(String word)
	{
		String trimmed = paragraph.trim();
		int count = 0;
		int wl = word.length();
		int tl = trimmed.length();
		
		// Case 1: User input is smaller than the word
		if (tl < wl) {
			return 0;
		}
		
		// Case 2: User input is the same length as the word
		if (tl == wl) {
			
			// Check if the user input is the same as word
			if (trimmed.substring(0, wl).equals(word)) {
				return 1;
			}
		}
		
		// Case 3: User input is longer than the word
		
		// Check first word and add to count if it matches
		if (trimmed.substring(0, wl).equals(word)) {
			count++;
		}
		
		// Check the last word and add to count if it matches
		
		/* trimmed.substring (tl - wl) 
		 * Checks the last word at index (tl-wl) and sees if it matches the word
		 * 
		 * trimmed.charAt(tl - wl - 1) == ' ')
		 * This condition makes sure that the character right before the word we're checking is a space aka the start of a new word
         * Checking spaces after the word doesn't matter because it will count as a failure anyway
		 */
	
		if ((trimmed.substring(tl - wl)).equals(word) && trimmed.charAt(tl - wl - 1) == ' ') {
			count++;
		}
		
		// Edge cases have been considered. Now to count the other words
		
		/*
		 * int i = wl so it skips the first word
		 * i < tl - wl so it skips the last word
		 */
		for (int i = wl; i < tl - wl; i++) {
			
			/* trimmed.substring(i, i + wl)).equals(word)
			 * Checks if the current word is a match
			 * 
			 * trimmed.charAt(i + wl) == ' '
			 * Checks to make sure the word is the same size as wl.
			 * It could be a match but part of a longer word. Ex: butter matching butterfly
			 * 
			 * trimmed.charAt(i - 1) == ' '
			 * Checks that index i is the start of a new word with space preceding it.
			 */
			if ((trimmed.substring(i, i + wl)).equals(word)
					&& trimmed.charAt(i + wl) == ' '
					&& trimmed.charAt(i - 1) == ' ')
			count++;
		}
		
		return count;
	}
}
