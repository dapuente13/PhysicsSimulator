package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	private List<Body> _bodies;
	private String[] columnNames = { "Id", "Mass", "Position", "Velocity", "Acceleration" };
	
	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int column) {
		return columnNames[column].toString();
	}
	
	public int getRowCount() {
		return _bodies.size();
	}
	
	// rowIndex = body
	// columnIndex = Id, Mass, Pos, Vel, Acc
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Object value = null;
		
		if (columnIndex == 0) {
			value = _bodies.get(rowIndex).getId();
		}
		else if (columnIndex == 1) {
			value = _bodies.get(rowIndex).getMass();
		}
		else if (columnIndex == 2) {
			value = _bodies.get(rowIndex).getPosition();
		}
		else if (columnIndex == 3) {
			value = _bodies.get(rowIndex).getVelocity();
		}
		else if (columnIndex == 4) {
			value = _bodies.get(rowIndex).getAccelration();
		}
		
		return value;
	}
	
	// observer functions
	
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies = bodies;
		fireTableStructureChanged();
	}
	
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies = bodies;
		fireTableStructureChanged();
	}

	public void onBodyAdded(List<Body> bodies, Body b) {
		_bodies = bodies;
		fireTableStructureChanged();
	}

	public void onAdvance(List<Body> bodies, double time) {
		_bodies = bodies;
		fireTableStructureChanged();
	}

	public void onDeltaTimeChanged(double dt) {
		
	}

	public void onGravityLawChanged(String gLawsDesc) {
		
	}

}
