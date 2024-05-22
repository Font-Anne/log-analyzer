
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Driver {

	public static void main(String[] args) throws Exception {
		
		/*-------------------READING FILE-------------------*/
		 File file = new File("C:\\Users\\5J3877897\\Documents\\VeeamBackupManager.log");
				 //"C:\\Users\\5J3877897\\Documents\\BDM Files\\java_multiline_test.txt");
		 
		 // Note:  Double back quote is to avoid incorrect compiler interpretation
		 // like \test as \t (ie. as a escape sequence)
		 
		 Scanner scanFile = new Scanner(file);
		 
		 List<String> fileInput = new ArrayList<String>();
		
		 // Condition holds true while there is character in a string
		 while (scanFile.hasNextLine())
		 {
			 // Turns all of the input into one big string because FreqCounter reads in String[] right now
			 //fileInput = fileInput + scanFile.nextLine().trim() + "\n";
			 String line = scanFile.nextLine().trim();
			 fileInput.add(line);
		 }
		
		 scanFile.close();
		 
		/*-------------------SCANNER USER INPUT-------------------*/
		
		// Allows the program to scan for user input
		Scanner scan = new Scanner(System.in);
		
		// Asks the user for any keywords they want to prioritize
		System.out.println("Specify any keywords you would like to look for, separate by spaces");
		System.out.println("Example: backups disconnect March stuck");
		
		String keywords = scan.nextLine().trim();
		String[] keysArr = keywords.split("\\s+");
		
		scan.close();
		
		FreqCounter freq = new FreqCounter(fileInput, keysArr);
		freq.printWordFrequency();
	}
}
