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
		wordTable = new LinkedHashMap<String, Integer>();
		lineTable = new LinkedHashMap<String, ArrayList<String>>();
		
		lineTable.put("error", new ArrayList<String>());
		lineTable.put("failure", new ArrayList<String>());
		lineTable.put("warning", new ArrayList<String>());
		lineTable.put("critical", new ArrayList<String>());
		
		wordTable.put("error", 0);
		wordTable.put("failure", 0);
		wordTable.put("warning", 0);
		wordTable.put("critical", 0);
		
		// Specify keywords based on user-input)
		for (String word : inKeywords)
		{
			if (!wordTable.containsKey(word))
			{
				wordTable.put(word, 0);
				lineTable.put(word, new ArrayList<String>());
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
			String mapKey = map.getKey();
			// Added escape characters \\Q and \\E so there are no issues even if the user wants to specify special characters in their input
			String regexInput = ".*\\Q" + mapKey + "\\E.*";
			
			Pattern thisPattern = Pattern.compile(regexInput, Pattern.CASE_INSENSITIVE);
			Matcher m = thisPattern.matcher(word);
			
			if (m.matches())
				return mapKey;
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
			
			//TODO: The issue is happening somewhere around here. If I give the code the input ".", it will add several words containing that character into wordTable
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
		
		// Printing by hashmap set causes it to not print in the order the words were added
		for (Map.Entry<String, Integer> map : wordTable.entrySet())
		{
			String thisWord = map.getKey();
			if (thisWord != null && !thisWord.isEmpty())
			{
				System.out.println(thisWord + " | Frequency Count = " + map.getValue());
				
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
