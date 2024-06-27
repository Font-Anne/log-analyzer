
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.io.FileNotFoundException;

public class Driver {

	public static void main(String[] args) throws Exception {
		
		MyFrame frame = new MyFrame();
		JButton button = frame.getButton();
		frame.setVisible(true);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == button) {
					
					FileFilter txtFilefilter = new FileFilter() {
						public boolean accept(File file) {
							if (file.isDirectory() || file.getName().endsWith(".txt") || file.getName().endsWith(".log")) {
								return true;
							}
							return false;
						}

						@Override
						public String getDescription() {
							return "Text or Log files only";
						}
					};
					
					JFileChooser file_upload = new JFileChooser();
					file_upload.setFileFilter(txtFilefilter);
					file_upload.setAcceptAllFileFilterUsed(false);
					
					int res = file_upload.showOpenDialog(null);
					
					if (res == JFileChooser.APPROVE_OPTION) {
						File file_path = new File(file_upload.getSelectedFile().getAbsolutePath());
						frame.setFilePath(file_path);
					}
					
					// Hide GUI when user has selected a file
					frame.setVisible(false);
					
					analyzeLogs(frame.getFilePath());
			
				// Automatically closes the window after program is run
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		
	}
	
	public static void analyzeLogs(File thisFile)
	{
		/*-------------------READING FILE-------------------*/
		 
		 //File file = new File("C:\\Users\\5J3877897\\Documents\\BDM Files\\zOS example log.txt");
		 
		 Scanner scanFile;
		 List<String> fileInput = new ArrayList<String>();
		try {
			 scanFile = new Scanner(thisFile);
				
			 // Condition holds true while there is character in a string
			 while (scanFile.hasNextLine())
			 {
				 // Turns all of the input into one big string because FreqCounter reads in String[] right now
				 //fileInput = fileInput + scanFile.nextLine().trim() + "\n";
				 String line = scanFile.nextLine().trim();
				 fileInput.add(line);
			 }
			
			 scanFile.close();
			 
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		/*-------------------SCANNER USER INPUT-------------------*/
		
		// Allows the program to scan for user input
		Scanner scan = new Scanner(System.in);
		
		// Asks the user for any keywords they want to prioritize
		System.out.println("Specify any keywords you would like to look for. Separate by spaces");
		System.out.println("Example: backups disconnect March stuck");
		
		String keywords = scan.nextLine().trim();
		String[] keysArr = keywords.split("\\s+");
		
		scan.close();
		
		FreqCounter freq = new FreqCounter(fileInput, keysArr);
		freq.printWordFrequency();
	}
}
