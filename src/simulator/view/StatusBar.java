package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ...
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies
	private JToolBar _toolBar;
	
	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
		
		// complete the code to build the tool bar
		
		_toolBar = new JToolBar();
		this.add(_toolBar, BorderLayout.PAGE_END);
		
		//----------------- Current Time -----------------------//
		
		JLabel t_desc = new JLabel();
		t_desc.setText("Time: ");
		_toolBar.add(t_desc);
		_currTime = new JLabel();
		_toolBar.add(_currTime);
		
		//Separator
		_toolBar.addSeparator(new Dimension(30,0));
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(2,0));
		_toolBar.add(sep);
		_toolBar.addSeparator(new Dimension(5,0));
		
		//----------------- # of Bodies---------------------//
		
		JLabel b_desc = new JLabel();
		b_desc.setText("Bodies: ");
		_toolBar.add(b_desc);
		_numOfBodies = new JLabel();
		_toolBar.add(_numOfBodies);
		_toolBar.addSeparator();
		
		//Separator
		_toolBar.addSeparator(new Dimension(20,0));
		JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
		sep2.setPreferredSize(new Dimension(2,0));
		_toolBar.add(sep2);
		_toolBar.addSeparator(new Dimension(5,0));

		//---------------- Gravity Law -------------------//
		
		JLabel g_desc = new JLabel();
		g_desc.setText("Laws: ");
		_toolBar.add(g_desc);
		_currLaws = new JLabel();
		_toolBar.add(_currLaws);
	}
	
	// other private/protected methods
	// ...
	
	// SimulatorObserver methods
	// ...
	

	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_currLaws.setText(gLawsDesc);
		_currTime.setText(time+"");
		_numOfBodies.setText(Integer.toString(bodies.size()));
	}
	
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_currLaws.setText(gLawsDesc);
		_currTime.setText(time+"");
		_numOfBodies.setText(Integer.toString(bodies.size()));
	}
	
	public void onBodyAdded(List<Body> bodies, Body b) {
		_numOfBodies.setText(Integer.toString(bodies.size()));
	}
	
	public void onAdvance(List<Body> bodies, double time) {
		_currTime.setText(time+"");
	}
	
	public void onDeltaTimeChanged(double dt) {
		
	}
	
	public void onGravityLawChanged(String gLawsDesc) {
		_currLaws.setText(gLawsDesc);
	}
}
