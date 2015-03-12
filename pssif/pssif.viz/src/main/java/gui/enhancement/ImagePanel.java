package gui.enhancement;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	private Image img = null;
	
	public Image getImg() {
		return img;
	}

	
	
	
	public void setImg(Image img) {
		this.img = img;
	}

	
	public ImagePanel(String img) {
		  this(new ImageIcon(img).getImage());
	  }
	
	  public ImagePanel(URL img) {
		  this(new ImageIcon(img).getImage());
	  }

	  public ImagePanel(Icon icon)
	  {
		  this(((ImageIcon) icon).getImage());
	  }
	  
	  public ImagePanel(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    
	    
	    setLayout(null);
	  } 
	  
	  public void paintComponent(Graphics g) {
	    	if (img != null)
	    	{
	    		g.drawImage(img, 10, 40, null);
	    	}
	  }
}