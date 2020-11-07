package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.*;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver{

	static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private boolean _stopped;
	private JButton load, glaw, play, stop, power;	
	private JSpinner steps;
	private JTextField delta;
	private InputStream is;
	private JToolBar _toolBar;
	private JFileChooser _fc;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);

	}
	
	private void initGUI() {
		
		_fc = new JFileChooser();

		setLayout(new BorderLayout());
		_toolBar = new JToolBar();
		this.add(_toolBar, BorderLayout.PAGE_START);		
		
		// Load button
		load = new JButton();
		load.setToolTipText("Load file");
		load.setIcon(new ImageIcon("resources/icons/open.png"));
		load.setPreferredSize(new Dimension(40, 40));
		load.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
			
		});;
		_toolBar.add(load);
		
		// Gravity Law button
		glaw = new JButton();
		glaw.setToolTipText("Select Gravity Law");
		glaw.setIcon(new ImageIcon("resources/icons/physics.png"));
		glaw.setPreferredSize(new Dimension(40, 40));
		glaw.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				selectGravityLaw(_ctrl.getGravityLawsFactory().getInfo());				
			}			
		});;
		_toolBar.add(glaw);		
		
		
		// Run button
		play = new JButton();
		play.setToolTipText("Play");
		play.setIcon(new ImageIcon("resources/icons/run.png"));
		play.setPreferredSize(new Dimension(40, 40));
		play.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {			
				play();
			}
			
		});;
		_toolBar.add(play);
		
		// Stop button
		stop = new JButton();
		stop.setToolTipText("Stop");
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		stop.setPreferredSize(new Dimension(40, 40));
		stop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
					_stopped = true;
			}
			
		});;
		_toolBar.add(stop);

		
		// Steps spinner
		_toolBar.add(new JLabel("Steps: "));
		steps = new JSpinner(new SpinnerNumberModel());
		steps.setValue(_ctrl.getSteps());
		steps.setToolTipText("Simulation steps to run");
		steps.setMaximumSize(new Dimension(80 , 40));
		steps.setMinimumSize(new  Dimension(80 , 40));
		steps.setPreferredSize(new Dimension(80 , 40));
		_toolBar.add(steps);
		_toolBar.addSeparator(); 
		
		//Delta time text field
		_toolBar.add(new JLabel("Delta-Time: "));
		delta = new JTextField();
		delta.setToolTipText("Delta time");
		delta.setMaximumSize(new Dimension(70 , 70));
		delta.setMinimumSize(new  Dimension(70 , 70));
		_toolBar.add(delta);
		
		_toolBar.add(Box.createGlue());
		_toolBar.addSeparator(); 

		
		// Exit button
		power = new JButton();
		power.setToolTipText("Exit");
		power.setIcon(new ImageIcon("resources/icons/exit.png"));
		power.setPreferredSize(new Dimension(40, 40));
		power.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == power) {
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (n == 0) {
						System.exit(0);
					}
				}
				
			}
		});;
		_toolBar.add(power);		

	}
	
	private void run_sim (int n) {
		
		if ( n>0 && !_stopped ) {
			
			try {
				_ctrl.run(1);
			}
			catch (Exception e) {
				// show the error in a dialog box
				JOptionPane.showMessageDialog(this, e.toString(),"Error", JOptionPane.WARNING_MESSAGE);
				
				// enable all buttons
				setEnable(true);
			
				_stopped = true;
				return;
			}
			
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				}
			}
			);
		} 
		else {
			_stopped = true;
			
			// enable all buttons
			setEnable(true);
		}
	}
	
	protected void loadFile() {
		int returnVal = _fc.showOpenDialog(this.getParent());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = _fc.getSelectedFile();

			try {
				is = new FileInputStream(file);
				_ctrl.reset();
				_ctrl.loadBodies(is);
			}
			
			catch(Exception e) {
				JOptionPane.showMessageDialog(this.getParent(), "Something went wrong while loading the file", "Error", JOptionPane.ERROR_MESSAGE );
			}
		}
	}
	
	protected void selectGravityLaw(List<JSONObject> glaw) {
		
		Object[] poss = new Object[glaw.size()];			
		
		for (int i = 0; i < glaw.size(); ++i) {
			poss[i]  = glaw.get(i).getString("desc") + " (" + glaw.get(i).getString("type") + ")";
		}
		
		
		String s = (String) JOptionPane.showInputDialog(this.getParent(), 
				"Select gravity laws to be used", "Gravity Laws Selector", JOptionPane.PLAIN_MESSAGE, null, poss,
				poss[1]);
		
		JSONObject gl = null;
		
		if (s!= null) {
			
			for (int i = 0; i < poss.length; ++i) {

				if (s.equals(poss[i])) {
					gl = _ctrl.getGravityLawsFactory().getInfo().get(i);
					break;
				}
			}
			
			_ctrl.setGravityLaws(gl);
		}
	}
	
	protected void play() {

		setEnable(false);
		
		_stopped = false;
		try {
			_ctrl.setDeltaTime(Double.parseDouble(delta.getText()));					
			run_sim((int) steps.getValue()); 
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, e1.toString(),"Exception", JOptionPane.WARNING_MESSAGE);
		}		
	}
	
	protected void setEnable(Boolean enable) {
		load.setEnabled(enable);
		glaw.setEnabled(enable);
		play.setEnabled(enable);
		power.setEnabled(enable);
		delta.setEnabled(enable);
		steps.setEnabled(enable);
	}
		
	
	// SimulatorObserver methods
		
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		delta.setText(dt+"");
	}

	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		delta.setText(dt+"");
	}

	public void onBodyAdded(List<Body> bodies, Body b) {
		
	}

	public void onAdvance(List<Body> bodies, double time) {
		
	}

	public void onDeltaTimeChanged(double dt) {
		delta.setText(dt+"");
	}

	public void onGravityLawChanged(String gLawsDesc) {
		
	}


}
