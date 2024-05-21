
public class PriorityWord implements Comparable<PriorityWord>
{
	private String word;
	private int priorityLevel;

	public PriorityWord(String w, int pLevel)
	{
		word = w;
		priorityLevel = pLevel;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public int getPriorityLevel()
	{
		return priorityLevel;
	}
	
	// Overriding the equals() method for this object because I want contains() for an ArrayList of this object to check if the word already exists in the list. There should not be more than once instance of Priority Word in a list because the purpose of the program is to count frequency.
	@Override
	public boolean equals(Object object)
	{
		boolean sameObj = false;
		
		if (object instanceof PriorityWord)
		{
			String objectWord = ((PriorityWord) object).getWord();
			
			// Rewrote the equals method for PriorityWord to count as a match as long as the object CONTAINS the word
			sameObj = this.word.equals(objectWord);
		}
		
		return sameObj;
	}
	
	@Override
	public int compareTo(PriorityWord pWord)
	{
		if (this.priorityLevel == pWord.getPriorityLevel())
			return 0;
		// In this implementation of the comparator, a lower priority level is a bigger value
		// Example: Priority Level 1 is bigger than Priority Level 2
		else if (this.priorityLevel < pWord.getPriorityLevel())
			return 1;
		else
			return -1;
	}
}
