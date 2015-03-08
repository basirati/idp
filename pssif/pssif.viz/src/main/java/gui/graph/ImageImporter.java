package gui.graph;

 
import gui.enhancement.ImagePanel;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
 
/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
@SuppressWarnings("serial")
public class ImageImporter extends JPanel
                             implements ActionListener {
    JButton openButton;
    JFileChooser fc;
    BufferedImage img;
    File file ;
    JLabel label;
    ImagePanel imgPanel = new ImagePanel("");
    
    public ImageImporter() {
        super(new BorderLayout());
 
        //Create a file chooser
        fc = new JFileChooser();
 
        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
 
        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Select an Image File...");
        openButton.addActionListener(this);
 
 
        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
 
        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
   //     add(logScrollPane, BorderLayout.CENTER);

    }
 
    public void actionPerformed(ActionEvent e) {
 
        //Handle open button action.
        if (e.getSource() == openButton) {
        	// Get array of available formats
        	String[] suffices = ImageIO.getReaderFileSuffixes();

        	// Add a file filter for each one
        	for (int i = 0; i < suffices.length; i++) {
        		FileNameExtensionFilter filter = new FileNameExtensionFilter(suffices[i] + " Files", suffices[i]);
        	    fc.addChoosableFileFilter(filter);
        	}

            int returnVal = fc.showOpenDialog(this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                this.remove(imgPanel);
                imgPanel = new ImagePanel(file.getPath());
                imgPanel.setSize(this.getSize());
                this.add(imgPanel);
                this.repaint();
            } 
        } 
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageImporter.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}