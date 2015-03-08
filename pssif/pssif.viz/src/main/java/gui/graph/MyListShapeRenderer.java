package gui.graph;

import graph.model.MyNodeType;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
/**
 * Allows to change the background color of a entry in a JList
 * @author Luc
 *
 */
public class MyListShapeRenderer extends DefaultListCellRenderer  
	{  
		private static final long serialVersionUID = 1L;
		private HashMap<MyNodeType, Image> shapeMapper;

	    public Component getListCellRendererComponent( JList list,  
	    		Object value, int index, boolean isSelected,  
	            boolean cellHasFocus )  
	    {  
	        super.getListCellRendererComponent( list, value, index,  
	                isSelected, cellHasFocus );  
	         
	        MyNodeType t = (MyNodeType) value;
	        if( shapeMapper.containsKey( t ) )  
	        {  
	            Image c = shapeMapper.get(t);

	          //here is where you should show the image in the box
//	            setBackground(c);
	        }  


	        return( this );  
	    }
	    
	    public void setColor (MyNodeType type , Image c)
	    {
	    	this.shapeMapper.put(type, c);
	    }
	    
	    public HashMap<MyNodeType, Image> getShapeMapping()
	    {
	    	return this.shapeMapper;
	    }
	    
	    public MyListShapeRenderer()
	    {
	    	super();
	    	shapeMapper = new HashMap<MyNodeType, Image>();
	    	
	    }
	    
	    public void setShapes(HashMap<MyNodeType,Image> map)
	    {
	    	this.shapeMapper = map;	
	    }
}
