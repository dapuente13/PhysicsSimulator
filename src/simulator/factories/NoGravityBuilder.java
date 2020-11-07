package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder<T> extends Builder<GravityLaws>{

	public NoGravityBuilder() {
		_type = "ng";
		_desc = "No gravity";
	}
	
	public GravityLaws createTheInstance(JSONObject info) {
		return new NoGravity();
	}

}
