package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	
	private PhysicsSimulator _ps;
	private Factory<Body> _body_factory;
	private Factory<GravityLaws> _gl_factory;
	private int _steps;
	
	public Controller(PhysicsSimulator ps, Factory<Body> body_factory, Factory<GravityLaws> gl_factory) {
		_ps = ps;
		_body_factory = body_factory;
		_gl_factory = gl_factory;
	}
	
	public void run (int n, OutputStream out) {
		PrintStream print = new PrintStream(out);
				
		_steps = n;
		
		print.println("{ \"states\": [ ");
		print.print(_ps.toString() + ", ");
		for (int i = 0; i < n; ++i) {
			_ps.advance();
			print.print(_ps.toString());
			if (i < n - 1) {
				print.println(", ");
			}
		}
		print.println("] }");
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray arr_body = jsonInput.getJSONArray("bodies");
		JSONObject body = null;
				
		for (int i = 0; i < arr_body.length(); ++i) {
			body = arr_body.getJSONObject(i);
			_ps.addBody(_body_factory.createInstance(body));
		}
	}
	
	public void reset() {
		_ps.reset();
	}
	
	public void setDeltaTime(double dt) {
		_ps.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		_ps.addObserver(o);
	}
	
	public void run(int n) {
		
		//runs the simulator for n steps, without printing anything to the console
		_steps = n;
		
		for (int i = 0; i < n; ++i) {
			_ps.advance();
		}
	}
	
	public Factory<GravityLaws> getGravityLawsFactory(){
		return _gl_factory;
	}
	
	public void setGravityLaws(JSONObject info) {
		_ps.setGravityLaws(_gl_factory.createInstance(info));
	}
	
	public int getSteps() {
		return _steps;
	}
	public void setSteps(int steps) {
		_steps = steps;
	}
}
