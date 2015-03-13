package gui.enhancement;

import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import gui.graph.GraphVisualization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;

public class ToolbarManager {
	
	public EnhancedToolBar createMouseToolbar(final GraphVisualization graph)
	{
		EnhancedToolBar ebt = new EnhancedToolBar(1);
		ebt.addButton("..//button//finger.png", new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			graph.setAbstractModalGraphMode(Mode.PICKING);
		}}, null, true);
		ebt.addButton("..//button//pick.png", new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			graph.setAbstractModalGraphMode(Mode.TRANSFORMING);
			graph.clearPickSupport();
		}}, null, true);
		

		//zoom in 
		ebt.addButton("..//button//zoom_in.jpg", new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e) {
			EnhancedVisualizationViewer ecv = (EnhancedVisualizationViewer) graph.getVisualisationViewer();
			ecv.subdivision_size+=10;
			ecv.repaint();
		} }, "Zoom In", true);
		
		//zoom out 
		ebt.addButton("..//button//zoom_out.jpg", new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e) {
			EnhancedVisualizationViewer ecv = (EnhancedVisualizationViewer) graph.getVisualisationViewer();
			ecv.subdivision_size-=10;
			ecv.repaint();
		} }, "Zoom Out", true);
		
		//Hide/Show Node Details 
		ebt.addButton("Hide/Show", new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e) {
			EnhancedVisualizationViewer ecv = (EnhancedVisualizationViewer) graph.getVisualisationViewer();
			graph.flipVertexLabelVisibility();
			graph.updateGraph();
			ecv.repaint();
					} }, "Hide Node Details", false);
	
		
		return ebt;
	}
	
	/**
	 * Create ToolBar With only new, import from file, and import from DB buttons
	 * 
	 * @return ToolBar
	 */
	public EnhancedToolBar createStandardToolBar(final FileCommands fcommands)
	{
		EnhancedToolBar ebt = new EnhancedToolBar(0);
		//new button
		ebt.addButton("..//button//newfile.png", new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e) {
			fcommands.createNewFile();
		} }, "New File", true);
		//import from file button
		ebt.addButton("..//button//importfile.png", new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e) {
			fcommands.importFromFile();
		} }, "Import File", true);
		//import from DB
		ebt.addButton("..//button//importdb.png", new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e) {
			fcommands.importFromDB();
		} }, "Import from DB", true);
	
		
		return ebt;
	}

}
