package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ...
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		//  complete this method to build the GUI
		
		// place the control panel at the PAGE_START of mainPanel panel
		ControlPanel cp = new ControlPanel(_ctrl);
		mainPanel.add(cp, BorderLayout.PAGE_START);
		
		// place the status bar at the PAGE_END of mainPanel panel
		StatusBar sb = new StatusBar(_ctrl);
		mainPanel.add(sb, BorderLayout.PAGE_END);
		
		// create new panel with BoxLayout (with BoxLayout.Y_AXIS)
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		// add bodies table to new panel
		BodiesTable bt = new BodiesTable(_ctrl);
		panel.add(new JScrollPane(bt));

		// add viewer to new panel
		Viewer vwr = new Viewer(_ctrl);
		vwr.setPreferredSize( new Dimension(200 ,200));
		vwr.setMaximumSize( new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		panel.add(vwr);
		
		
		// place the new panel at the CENTER of mainPanel
		mainPanel.add(panel, BorderLayout.CENTER);
		

		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1370, 730);
		this.setVisible(true);
		


	}
	
	// other private/protected methods
	// ...
}