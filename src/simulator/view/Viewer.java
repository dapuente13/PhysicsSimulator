package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
	

	private static final long serialVersionUID = 1L;
	
	// ...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	private String help = "h: toggle help,  +: zoom-in,  -: zoom-out,  =: fit";
	
	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2),
				"Viewer",
				TitledBorder.LEFT, TitledBorder.TOP));
		setVisible(true);
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;		
	
	
		addKeyListener(new KeyListener() {
			// ...
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
					case '-':
						_scale = _scale * 1.1;
						break;
					case '+':
						_scale = Math.max(1000.0, _scale / 1.1);
					break;
					case '=':
						autoScale();
					break;
					case 'h':
						_showHelp = !_showHelp;
						break;
					default:
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
	
		addMouseListener(new MouseListener() {
			// ...
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		// use ’gr’ to draw not ’g’
		
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
	
		
		// draw bodies
		for (int i = 0; i < _bodies.size(); i++) {
			drawCircle(_bodies.get(i).getPosition().coordinate(0), _bodies.get(i).getPosition().coordinate(1), gr, _bodies.get(i).getId());
		}
		
		// draw help if _showHelp is true
		if (_showHelp) {
			gr.setColor(Color.RED);
			gr.drawString(help, 10, 30);
			gr.drawString("Scaling ratio:  " + _scale, 10, 45);
		}
		
		// draw a cross at center
		gr.setColor(Color.RED);		
		gr.drawLine(_centerX + 3, _centerY - 3, _centerX + 3, _centerY + 8);
		gr.drawLine(_centerX - 3, _centerY + 3, _centerX + 8, _centerY + 3);


	}
	
	// other private/protected methods
	// ...
	
	private void autoScale() {
		double max = 1.0;
	
		for (Body b : _bodies) {
			Vector p = b.getPosition();
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max, Math.abs(b.getPosition().coordinate(i)));
		}
	
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	protected void drawCircle(double x, double y, Graphics gr, String name) {
		// Id
		gr.setColor(Color.BLACK);
		gr.drawString(name, _centerX-3 + (int) (x/_scale), _centerY-3 - (int) (y/_scale));
		
		// Circle
		gr.setColor(Color.BLUE);
		gr.drawOval(_centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 5, 5);
		gr.fillOval(_centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 5, 5);
	}

	
	
	// SimulatorObserver methods
	// ...
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies = bodies;
		autoScale();
		repaint();
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies = bodies;
		autoScale();
		repaint();
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_bodies = bodies;
		autoScale();
		repaint();
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_bodies = bodies;
		repaint();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		
	}
	

}