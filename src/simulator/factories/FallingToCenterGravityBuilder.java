package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;


public class FallingToCenterGravityBuilder<T> extends Builder<GravityLaws> {

	public FallingToCenterGravityBuilder() {
		_type = "ftcg";
		_desc = "Falling to center gravity";
	}
	
	public GravityLaws createTheInstance(JSONObject info) {

		return new FallingToCenterGravity();
	}

	
}
