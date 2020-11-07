package simulator.model;
import simulator.misc.Vector;

public class MassLosingBody extends Body{

	protected double _lossFactor, _lossFrequency, c = 0.0; 
	protected String _id;
	protected double _mass;
	protected Vector _pos, _acc, _vel;
	
	public MassLosingBody(String id, Vector vel, Vector pos, Vector acc, double mass, double lossFactor, double lossFrequency) {
		super(id, vel, pos, acc, mass);
		
		_lossFactor = lossFactor;
		_lossFrequency = lossFrequency;
	}
	
	void move(double t) {

		super.move(t);
				
		c += t;
				
		if (c >= _lossFrequency) {
			_mass = _mass * (1 -_lossFactor);
			c = 0.0;
		}
	}

}
