package gui.graph;

import graph.model.MyNodeType;
import gui.GraphView;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.ModelBuilder;

public class NodeShapePopup extends MyPopup{
	
	private JPanel panel;
	private JList<MyNodeType> nodeTypeList;
	private MyListShapeRenderer shapelistener;
	private GraphView graphView;
	
	public NodeShapePopup(GraphView graphView)
	{
		this.graphView = graphView;
	}
	

	/**
	 * Create the Panel(GUI) of the Popup 
	 * @return a panel with all the components
	 */
	private JPanel createPanel()
	{
		JPanel bannerPanel = new JPanel(new GridLayout());
		
		MyNodeType[] nodetypes = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
		
		Arrays.sort(nodetypes, new MyNodeTypeComparator());
		
		shapelistener = new MyListShapeRenderer();
		shapelistener.setShapes(graphView.getGraph().getNodeColorMapping());
	    
	    nodeTypeList.setCellRenderer(shapelistener);
		
		nodeTypeList = new JList<MyNodeType>( nodetypes );  
	    nodeTypeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	     
	    JScrollPane sp = new JScrollPane( nodeTypeList ); 
	    bannerPanel.add(sp);
	    
	    bannerPanel.add(new ImageImporter());
	    return bannerPanel;
	}
	
	/**
	 * Evaluate the Popup after the users input
	 * @param dialogResult the result of the users interaction with the popup gui
	 */
	private void evalDialog (int dialogResult)
	{
		if (dialogResult==0)
	 	{
	 		HashMap<MyNodeType, Image > shapes = shapelistener.getShapeMapping();
	 		graphView.getGraph().setNodeColorMapping(shapes);
	 		
	 	}
	}
	
	/**
	 * Display the Popup to the user
	 */
	public void showPopup()
	{
		panel = createPanel();
		
		JOptionPane.showConfirmDialog(null, panel, "Choose Nodes Shapes", JOptionPane.DEFAULT_OPTION);

	}
}
