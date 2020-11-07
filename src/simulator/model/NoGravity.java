package simulator.model;
import java.util.List;

public class NoGravity implements GravityLaws {
	
	public NoGravity() {}

	public void apply(List<Body> bodies) { //has to be empty 
	
	}
	
	public String toString() {
		return "No gravity";
	}
	
}
