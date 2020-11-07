package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder<T> extends Builder<GravityLaws>{

	public NewtonUniversalGravitationBuilder() {
		_type = "nlug";
		_desc = "Newton's law of universal gravitation";
	}
	
	public GravityLaws createTheInstance(JSONObject info) {
		return new NewtonUniversalGravitation();
	}

	
}
