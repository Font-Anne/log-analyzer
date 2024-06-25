
import java.awt.*;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JButton;

public class MyFrame extends JFrame {

	JButton btn;
	File file_path;
	
	MyFrame()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Demo");
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout());
		
		btn = new JButton("Upload file");
		btn.setFocusable(false);
		this.add(btn);
		
		// Set to empty String to avoid any null object errors
		file_path = null;
		
		this.pack();
		this.setVisible(true);
	}

	
	public JButton getButton()
	{
		return btn;
	}
	
	public void setFilePath(File inFile)
	{
		file_path = inFile;
	}
	
	public File getFilePath()
	{
		return file_path;
	}
}
