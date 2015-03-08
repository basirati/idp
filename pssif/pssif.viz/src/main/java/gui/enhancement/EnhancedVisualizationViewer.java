package gui.enhancement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;





import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import edu.uci.ics.jung.visualization.util.Caching;
import graph.model.IMyNode;
import graph.model.MyEdge;

@SuppressWarnings("serial")
public class EnhancedVisualizationViewer extends VisualizationViewer<IMyNode, MyEdge> implements MouseWheelListener{

	public int subdivision_size = 30; 
	
	public EnhancedVisualizationViewer(Layout<IMyNode, MyEdge> layout) {
		super(layout);
		setBackground(Color.WHITE);
	    setForeground(Color.BLACK);	
	    setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.addMouseWheelListener(this);
	}

	public void paintGrid(Graphics2D g2)
	{
		int SUBDIVISIONS_WIDTH = getSize().width / subdivision_size;
        int SUBDIVISIONS_HEIGHT = getSize().height / subdivision_size;
      
        for (int i = 1; i < SUBDIVISIONS_WIDTH; i++) {
        	g2.setColor(Color.LIGHT_GRAY);
        	if ((i-(i/4)*4) == 0)
        		g2.setPaint(Color.DARK_GRAY);
           int x = i * subdivision_size;
           g2.drawLine(x, 0, x, getSize().height);
        }
        for (int i = 1; i < SUBDIVISIONS_HEIGHT; i++) {
        	g2.setColor(Color.LIGHT_GRAY);
        	if ((i-(i/4)*4) == 0)
        		g2.setPaint(Color.DARK_GRAY);
           int y = i * subdivision_size;
           g2.drawLine(0, y, getSize().width, y);
           
        }   
  
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
    	
    	int notches = e.getWheelRotation();
    	if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
    	{
    		if (notches < 0)
    		{
    			if (subdivision_size > 10)		
    				subdivision_size = subdivision_size + ((notches)*10);
    		}
    		else
    		{
    			if (subdivision_size < 140)
    				subdivision_size = subdivision_size + ((notches)*10);
    		}
    	}
    	this.repaint();
    }

	@Override
	protected void renderGraph(Graphics2D g2d) {
	    if(renderContext.getGraphicsContext() == null) {
	        renderContext.setGraphicsContext(new GraphicsDecorator(g2d));
        } else {
        	renderContext.getGraphicsContext().setDelegate(g2d);
        }
        renderContext.setScreenDevice(this);
	    Layout<IMyNode,MyEdge> layout = model.getGraphLayout();

		g2d.setRenderingHints(renderingHints);
		
		// the size of the VisualizationViewer
		Dimension d = getSize();
		
		// clear the offscreen image
		g2d.setColor(getBackground());
		g2d.fillRect(0,0,d.width,d.height);
		
		// painting the background grid area
		paintGrid(g2d);

		AffineTransform oldXform = g2d.getTransform();
        AffineTransform newXform = new AffineTransform(oldXform);
        newXform.concatenate(
        		renderContext.getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform());
//        		viewTransformer.getTransform());
		
        g2d.setTransform(newXform);

		// if there are  preRenderers set, paint them
		for(Paintable paintable : preRenderers) {

		    if(paintable.useTransform()) {
		        paintable.paint(g2d);
		    } else {
		        g2d.setTransform(oldXform);
		        paintable.paint(g2d);
                g2d.setTransform(newXform);
		    }
		}
		
        if(layout instanceof Caching) {
        	((Caching)layout).clear();
        }
        
        renderer.render(renderContext, layout);
		
		// if there are postRenderers set, do it
		for(Paintable paintable : postRenderers) {

		    if(paintable.useTransform()) {
		        paintable.paint(g2d);
		    } else {
		        g2d.setTransform(oldXform);
		        paintable.paint(g2d);
                g2d.setTransform(newXform);
		    }
		}
		g2d.setTransform(oldXform);
	}

}
