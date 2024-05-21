import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FreqCounter {
	//private String paragraph;
	private List<String> paragraph;
	
	//private HashMap<String, Integer> wordTable;
	private HashMap<String, Integer> wordTable;
	
	// Lines are only tracked for Priority 1 and Priority 2 (user specified) words
	//private HashMap<String, ArrayList<String>> lineTable;
	private HashMap<String, ArrayList<String>> lineTable;
	
	private List<String> regexTracker;
	//private List<PriorityWord> pWordList;
	
	public FreqCounter(List<String> fileInput, String[] inKeywords)
	{
		// Passing an ArrayList is pass by value. paragraph contains the address to the original ArrayList so be very careful about doing anything but get() on this ArrayList
		paragraph = fileInput;
		wordTable = new HashMap<String, Integer>();
		lineTable = new HashMap<String, ArrayList<String>>();
		
		// Specify the Priority Level 1 Keywords (Most Important)
		regexTracker = new ArrayList<String>();
		//pWordList = new ArrayList<PriorityWord>();
		
		// Define pre-determined keywords to prioritize when searching the text here. Second parameter is the priority level (higher = more important)
		/*
		pWordList.add(new PriorityWord("error", 1));
		pWordList.add(new PriorityWord("errors", 1));
		pWordList.add(new PriorityWord("failure", 1));
		pWordList.add(new PriorityWord("warning", 1));
		pWordList.add(new PriorityWord("critical", 1));*/

		regexTracker.add(".*error.*");
		regexTracker.add(".*failure.*");
		regexTracker.add(".*warning.*");
		regexTracker.add(".*critical.*");
		
		
		lineTable.put(".*error.*", new ArrayList<String>());
		lineTable.put(".*failure.*", new ArrayList<String>());
		lineTable.put(".*warning.*", new ArrayList<String>());
		lineTable.put(".*critical.*", new ArrayList<String>());
		
		// Specify the Priority Level 2 Keywords (Based on user-input)
		for (String word : inKeywords)
		{
			String keywordToRegex = ".*" + word + ".*";
			if (!regexTracker.contains(keywordToRegex))
			{
				regexTracker.add(keywordToRegex);
				lineTable.put(keywordToRegex, new ArrayList<String>());
			}

			/*
			PriorityWord p2Keyword = new PriorityWord(word, 2);
			if (!pWordList.contains(p2Keyword))
			{
				pWordList.add(p2Keyword);
				lineTable.put(word, new ArrayList<String>());
			}*/
		}
		
		getFreqList();
	}
	
	// This method takes a word and checks whether any regular expression in regexTracker has a Pattern that matches the word.
	// If a match is found, it returns the regular expression the word matched with.
	public String findExpression(String word)
	{
		for (int k = 0; k < regexTracker.size(); k++)
		{
			String regexInput = regexTracker.get(k);
			
			Pattern thisPattern = Pattern.compile(regexInput, Pattern.CASE_INSENSITIVE);
			Matcher m = thisPattern.matcher(word);
			
			if (m.matches())
				return regexInput;
		}
		
		return "NO_MATCH_FOUND";
	}
	
	public void getFreqList()
	{	
		
		// Loops through array of lines
		for (int k = 0; k < paragraph.size(); k++)
		{	
			String line = paragraph.get(k);
			
			// Note that this is a nested For Loop. This loop goes through all words in the line while the outer loop goes through all lines in the file.
			// Maybe there's a more efficient way to implement this. Something to consider.
			addWordsToTable(line, k + 1);
		}
	}
	
	public void addWordsToTable(String line, int lineCount)
	{
		// Creates an array of all words in the paragraph.
		// "\\s+" pattern matches for any whitespace character and the plus indicates if there's one or more of the character
		String trimmed = line.trim().toLowerCase();
		String[] wordsInLine = trimmed.split("\\s+");
		
		// Use thisLine to record the line where you found the word. Also created a boolean that it only records once even if the word appears several times in one line
		boolean alreadyFound = false;
		
		for (String word : wordsInLine)
		{	
			
			String regexMatch = findExpression (word);
			
			if (!regexMatch.equals("NO_MATCH_FOUND"))
			{
				if (!wordTable.containsKey(regexMatch))
					wordTable.put(regexMatch, 1);
				else
					wordTable.put(regexMatch, wordTable.get(regexMatch) + 1);
			}
			
			// A line is only stored the line after finding it the first time
			if (lineTable.containsKey(regexMatch) && !alreadyFound)
			{
				
			}
			
			/*PriorityWord newKeyword = new PriorityWord(word, 3);
			
			if (pWordList.contains(newKeyword))
			{
				if (!wordTable.containsKey(word))
					wordTable.put(word, 1);
				else
					wordTable.put(word, wordTable.get(word) + 1);
			}
			
			if (lineTable.containsKey(word) && !alreadyFound)
			{
					lineTable.get(word).add("Line " + lineCount + ": " + line);
					alreadyFound = true;
			}*/
		}
	}
	
	public void printWordFrequency()
	{
		// Formatting can be improved but right now it's working as intended. Might want to consider case for larger data set.
		System.out.println("--------- Word Frequencies ---------");
		
		// Prints word count results for all specified keywords ordered by Priority Level
		
		for (int k = 0; k < pWordList.size(); k++)
		{
			PriorityWord pWord = pWordList.get(k);
			String thisWord = pWord.getWord();
			//int thisPriority = pWord.getPriorityLevel();
			
			Object wordValue = wordTable.get(thisWord);
			if (wordValue != null)
			{
				// Tests whether words are printing in correct Priority order
				//System.out.println(thisWord + ":\t" + wordValue + "\t" + thisPriority);
				
				System.out.println(thisWord + " | Frequency Count = " + wordValue);
				
				if (lineTable.containsKey(thisWord))
				{
					ArrayList<String> lineList = lineTable.get(thisWord);
					
					//Changed so that it will stop printing error lines after the first 10
					int i = 0;
					
					while (i < lineList.size() && i <= 10)
					{
						System.out.println(lineList.get(i));
						i++;
					}
					
					System.out.println();
				}
			}
		}
	}
}
