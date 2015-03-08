package gui.enhancement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	private Image img = null;
	private Shape shape = null;
	boolean viewer = false;
	
	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	
	
	final float dash1[] = {10.0f};
    final BasicStroke dashed = new BasicStroke(1.0f, 
                                                      BasicStroke.CAP_BUTT, 
                                                      BasicStroke.JOIN_MITER, 
                                                      10.0f, dash1, 0.0f);
	  public ImagePanel(String img) {
			  this(new ImageIcon(img).getImage());
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
	  
	  public void changeImage(String img)
	  {
		  this.img = new ImageIcon(img).getImage();
		  Dimension size = new Dimension(this.img.getWidth(null), this.img.getHeight(null));
		  setPreferredSize(size);
		  setMinimumSize(size);
		  setMaximumSize(size);
		  setSize(size);
		  setLayout(null);
	  }

	 
	  
	  public void paintComponent(Graphics g) {
	    	if (img != null)
	    	{
	    		g.drawImage(img, 10, 10, null);
	    		Graphics2D g2 = (Graphics2D) g;
	    		if (viewer)
	    		{
	    			g2.setPaint(Color.GRAY);
	    			g2.setStroke(dashed);
	    			g2.draw(new RoundRectangle2D.Double(0, 0, img.getWidth(this)+20, 
	    				img.getHeight(this)+20, -5, -5));
	    		}
	    	}
	    	if (shape != null)
	    	{
	    		Graphics2D g2 = (Graphics2D) g;
	    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    		g2.draw(shape);
	    		g2.setPaint(Color.GRAY);
	    		g2.setStroke(dashed);
	            g2.draw(new RoundRectangle2D.Double(0, 0, shape.getBounds2D().getWidth()+20, 
	                                                shape.getBounds2D().getHeight()+20, -5, -5));
	    	}
	  }
}