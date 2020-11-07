package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder<T> extends Builder<Body>{
	
	private Vector pos, vel, acc;
	private String id;
	private double mass, freq, factor;

	public MassLosingBodyBuilder() {	
		_type = "mlb";
		_desc = "Mass Losing Body";
	}
	
	public Body createTheInstance(JSONObject info) {
		
		MassLosingBody mlb = null;
		JSONObject data = new JSONObject();
		data = info.getJSONObject("data");
		
		if (!info.isEmpty()) {
			
			id = data.getString("id");
			
			pos = new Vector(jsonArrayTodoubleArray(data.getJSONArray("pos")));
			
			vel = new Vector(jsonArrayTodoubleArray(data.getJSONArray("vel")));
			
			acc = new Vector(pos.dim());
						
			mass = data.getDouble("mass");
			
			factor = data.getDouble("factor");
			
			freq = data.getDouble("freq");
			
			
			mlb = new MassLosingBody(id, vel, pos, acc, mass, factor, freq);
		}
		
		return mlb;
	}
	
	protected JSONObject createData() {
		
		JSONObject data = new JSONObject();
		data.put("id", "body id");
		data.put("pos", "body position");
		data.put("vel", "body velocity");
		data.put("acc", "body acceleration");
		data.put("mass", "body mass");
		data.put("factor", "mass loss factor");
		data.put("freq", "mass loss frequency");
		
		return data;
	}

}
