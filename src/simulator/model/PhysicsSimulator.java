package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.model.Body;

public class PhysicsSimulator {

	private GravityLaws _gl;
	private double _actual_time = 0.0, _dt;
	private List<Body> body_list = new ArrayList<>();
	private List<SimulatorObserver> o_list = new ArrayList<>();

	public PhysicsSimulator(GravityLaws gl, double dt) {

		if (gl == null)
			throw new IllegalArgumentException();
		if (dt <= 0.0)
			throw new IllegalArgumentException();

		_gl = gl;
		_dt = dt;
	}
	
	public void addBody(Body body) {

		// ----------------Prove body is not in list------------------//
		
		if (body.equals(null)) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < body_list.size(); ++i) {
			if (body.getId().equals(body_list.get(i).getId())) {
				throw new IllegalArgumentException();
			}
		}

		// -----------------------Add body---------------------------//

		body_list.add(body);
		
		//----send an onBodyAdded notification to all observers-----//
		
		for(int i = 0; i < o_list.size(); i++) {
			o_list.get(i).onBodyAdded(body_list, body);;
		}
	}

	public void advance() {

		
		_gl.apply(body_list);

		for (int i = 0; i < body_list.size(); ++i) {
			body_list.get(i).move(_dt);
		}

		_actual_time += _dt;
		
		//----send an onAdvance notification to all observers-----//
		
		for(int i = 0; i < o_list.size(); i++) {
			o_list.get(i).onAdvance(body_list, _actual_time);
		}
	}

	public String toString() {
		String str = "{ \"time\": " + _actual_time + ", \"bodies\": [ ";

		for (int i = 0; i < body_list.size(); ++i) {
			str += body_list.get(i).toString();
			if (i < body_list.size() - 1) {
				str += ", ";
			}
		}

		return str + "] }";
	}
	
	public void reset() {
		//  clears the list of bodies and sets the current time to 0.0.
		_actual_time = 0.0;
		body_list = new ArrayList<>();
		
		//----send an onReset notification to all observers-----//
		
		for(int i = 0; i < o_list.size(); i++) {
			o_list.get(i).onReset(body_list, _actual_time, _dt, _gl.toString());
		}
	}

	public void setDeltaTime(double dt) {
		
		//changes the current value of the delta-time
		if (dt <= 0)
			throw new IllegalArgumentException();
		
		_dt = dt;
		
		//----send an onDeltaTimeChanged notification to all observers-----//
		
		for(int i = 0; i < o_list.size(); i++) {
			o_list.get(i).onDeltaTimeChanged(_dt);
		}
	}
	
	public void setGravityLaws(GravityLaws gravityLaws) {
		
		if (gravityLaws.equals(null))
			throw new IllegalArgumentException();
		
		_gl = gravityLaws;

		
		//----send an onGravityLawsChanged notification to all observers-----//
		
		for(int i = 0; i < o_list.size(); i++) {
			o_list.get(i).onGravityLawChanged(_gl.toString());
		}
	}
	
	public void addObserver(SimulatorObserver o) { 	//add o to the list of observers, if it is not there already

		
		// ----------------Prove observer is not in list------------------//

		if (o.equals(null)) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < o_list.size(); ++i) {
			if(o.equals(o_list.get(i)))
				throw new IllegalArgumentException();
		}

		// -----------------------Add observer---------------------------//

		o_list.add(o);
		
		/* send an onRegister notification only to the new
		observer in order to pass it the current state of the simulator. */
		
		o.onRegister(body_list, _actual_time, _dt, _gl.toString());

	}
	
}
