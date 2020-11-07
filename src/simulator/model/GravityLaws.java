package simulator.model;
import java.util.List;
import simulator.model.Body;

public interface GravityLaws {
	
	public void apply(List<Body> bodies);
	
	public String toString();
}
