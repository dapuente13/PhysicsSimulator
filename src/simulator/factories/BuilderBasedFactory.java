package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> _builders;
	private List<JSONObject> _info;

	public BuilderBasedFactory(List<Builder<T>> builders) {
		_builders = new ArrayList<>(builders);
		_info = new ArrayList<JSONObject>();
		for (int i = 0; i < _builders.size(); ++i) {
			_info.add(_builders.get(i).getBuilderInfo());
		}

	}

	public T createInstance(JSONObject info) {

		T obj = null;

		for (int i = 0; i < _builders.size(); ++i) {
			obj = _builders.get(i).createInstance(info);

			if (obj != null) {
				return obj;
			}
		}

		return obj;
	}

	public List<JSONObject> getInfo() {
		return _info;
	}

}
