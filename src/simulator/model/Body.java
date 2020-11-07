package simulator.model;
import simulator.misc.Vector;

public class Body {
		
	private String _id;
	private double _mass;
	private Vector _pos, _acc, _vel;
	
	public Body(String id, Vector vel, Vector pos, Vector acc, double mass){
		_id = id;
		_vel = vel;
		_pos = pos;
		_acc = acc;
		_mass = mass;
	}
	
	public String getId() {
		return _id;
	}
	
	public Vector getVelocity() {
		return _vel;
	}
	
	public Vector getAccelration() {
		return _acc;
	}
	
	public Vector getPosition() {
		return _pos;
	}
	
	public double getMass() {
		return _mass;
	}
	
	void setVelocity(Vector v) {
		_vel = v;
	}
	
	void setAcceleration(Vector a) {
		_acc = a;
	}
	
	void setPosition(Vector p) { 
		_pos = p;
	}
	
	void move(double t) {
		setPosition(_pos.plus(_vel.scale(t)).plus(_acc.scale(Math.pow(t, 2) * 0.5)));		// p = p + v*t + 1/2*a*t^2
		setVelocity(_vel = _vel.plus(_acc.scale(t)));												// v = v + a*t
	}
	
	
	
	public String toString() {
		return  "{ \"id\": \"" + _id + "\",  \"mass\": " + _mass + 
				",  \"pos\": " + _pos.toString() + ",  \"vel\": " + _vel.toString() + ",  \"acc\": " + _acc.toString() + " }";
	}
}

