package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T> {
	
	protected String _type;
	protected String _desc;

	public Builder() {	}
	
		
	protected double[] jsonArrayTodoubleArray(JSONArray arr) {
		
		double[] d_arr = new double[arr.length()];
		
		if (!arr.isEmpty()) {
			for (int i = 0; i < arr.length(); ++i) {
				d_arr[i] = arr.getDouble(i);
			}
		}
		
		return d_arr;
	}
	
	public T createInstance(JSONObject info) {
		
		T obj = null;
		
		if ( _type.equals(info.getString("type"))) {
			obj = createTheInstance(info);
		}
	
		return obj;
	}
	
	public JSONObject getBuilderInfo() {
		JSONObject info = new JSONObject();
		info.put("type", _type);
		info.put("desc", _desc);
		info.put("data", createData());

		return info;
	}
	
	protected JSONObject createData() {
		
		return new JSONObject();
	}
	
	protected abstract T createTheInstance(JSONObject info);
	
}
