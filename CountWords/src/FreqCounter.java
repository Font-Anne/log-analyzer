import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FreqCounter {
	//private String paragraph;
	private List<String> paragraph;
	private HashMap<String, Integer> wordTable;
	
	// Lines are only tracked for hard-coded and user-specified words
	private HashMap<String, ArrayList<String>> lineTable;
	
	public FreqCounter(List<String> fileInput, String[] inKeywords)
	{
		// Passing an ArrayList is pass by value. paragraph contains the address to the original ArrayList so be very careful about doing anything but get() on this ArrayList
		paragraph = fileInput;
		wordTable = new HashMap<String, Integer>();
		lineTable = new HashMap<String, ArrayList<String>>();
		
		lineTable.put(".*error.*", new ArrayList<String>());
		lineTable.put(".*failure.*", new ArrayList<String>());
		lineTable.put(".*warning.*", new ArrayList<String>());
		lineTable.put(".*critical.*", new ArrayList<String>());
		
		wordTable.put(".*error.*", 0);
		wordTable.put(".*failure.*", 0);
		wordTable.put(".*warning.*", 0);
		wordTable.put(".*critical.*", 0);
		
		// Specify the Priority Level 2 Keywords (Based on user-input)
		for (String word : inKeywords)
		{
			// TODO: Need to change implementation to account for inputs that may contain "*" or "." character.
			// Escape characters should be added for anything that could be read as regex formatting
			String keywordToRegex = ".*" + word + ".*";
			
			if (!wordTable.containsKey(keywordToRegex))
			{
				wordTable.put(keywordToRegex, 0);
				lineTable.put(keywordToRegex, new ArrayList<String>());
			}
		}
		
		getFreqList();
	}
	
	// This method takes a word and checks whether any regular expression in regexTracker has a Pattern that matches the word.
	// If a match is found, it returns the regular expression the word matched with.
	public String findExpression(String word)
	{
		for (Map.Entry<String, Integer> map : wordTable.entrySet())
		{
			String regexInput = map.getKey();
			
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
				lineTable.get(regexMatch).add("Line " + lineCount + ": " + line);
				alreadyFound = true;
			}
		}
	}
	
	public void printWordFrequency()
	{
		// Formatting can be improved but right now it's working as intended. Might want to consider case for larger data set.
		System.out.println("--------- Word Frequencies ---------");
		
		// Prints word count results for all specified keywords ordered by Priority Level
		
		for (Map.Entry<String, Integer> map : wordTable.entrySet())
		{
			String thisExpression = map.getKey();
			if (thisExpression != null)
			{
				int start = thisExpression.indexOf("*");
				int end = thisExpression.lastIndexOf(".");
				
				// Even empty String case ".*.* and case with multiple symbols ".**..**..*"should work so I will not check for OOB error
				// If this case is failing, then it should be looked at to see what input is causing problems.
					
				String regexToWord = thisExpression.substring(start + 1, end);
				
				if (!regexToWord.isEmpty())
					System.out.println(regexToWord + " | Frequency Count = " + map.getValue());
				
				if (lineTable.containsKey(thisExpression))
				{
					ArrayList<String> lineList = lineTable.get(thisExpression);
					
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
