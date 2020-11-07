package simulator.model;
import java.util.List;

public class FallingToCenterGravity implements GravityLaws{
	
	private final  double g = 9.81;

	public FallingToCenterGravity() {}
	
	public void apply(List<Body> bodies) {
		
		for (int i = 0; i < bodies.size(); i++) {	
			bodies.get(i).setAcceleration((bodies.get(i).getPosition().direction()).scale(-g));		// a = -g * p
		}
	}

	public String toString() {
		return "Falling to center gravity";
	}
} 