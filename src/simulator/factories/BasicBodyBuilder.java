package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder<T> extends Builder<Body>{
	
	private Vector pos, vel, acc;
	private String id;
	private double mass;
	
	public BasicBodyBuilder() {	
		_type = "basic";
		_desc = "basic body";
	}
	
	public Body createTheInstance(JSONObject info) {
		
		Body  b = null;
		JSONObject data = new JSONObject();
		data = info.getJSONObject("data");
		
		if (!data.isEmpty()) {
			
			id = data.getString("id");
						
			pos = new Vector(jsonArrayTodoubleArray(data.getJSONArray("pos")));
			
			vel = new Vector(jsonArrayTodoubleArray(data.getJSONArray("vel")));
			
			acc = new Vector(pos.dim());			
			
			mass = data.getDouble("mass");
			
			b =  new Body(id, vel, pos, acc, mass);
		}
		
		return b;
	}

	protected JSONObject createData() {
		
		JSONObject data = new JSONObject();
		data.put("id", "body id");
		data.put("pos", "body position");
		data.put("vel", "body velocity");
		data.put("acc", "body acceleration");
		data.put("mass", "body mass");
		
		return data;
	}
}
