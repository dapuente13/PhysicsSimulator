package simulator.model;
import java.util.List;
import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws{
	
	private final double G = 6.67E-11;

	public NewtonUniversalGravitation() {}	
	
	
	public void apply(List<Body> bodies) {		
		Vector sum_force;
		
		for (int i = 0; i < bodies.size(); i++) {
			
			sum_force = new Vector(bodies.get(i).getPosition().dim());
			
			for (int j = 0; j < bodies.size(); j++) {
				if (i != j) {
					sum_force = sum_force.plus(formula(bodies.get(i), bodies.get(j)));		// sum of all the body forces
				}
			}
			if (1/bodies.get(i).getMass() == 0) {
				bodies.get(i).setAcceleration(new Vector(bodies.get(i).getPosition().dim()));
			}
			else {
				bodies.get(i).setAcceleration(sum_force.scale(1/bodies.get(i).getMass()));		// F = m * a ====> a = F / m
			}
		}
	}
	
	public Vector formula(Body b1, Body b2) {
		
		double f, d = b2.getPosition().distanceTo(b1.getPosition());				// d = |p - p|^2       
		Vector direction = b2.getPosition().minus(b1.getPosition()).direction(); 	// direction = p - p
 
		f = G * (b1.getMass() * b2.getMass())/(d*d);								// Newton's law of universal gravitation
		
		return direction.scale(f);													// F = d * f
	}
	
	public String toString() {
		return "Newton's Universal Gravitation";
	}
}
