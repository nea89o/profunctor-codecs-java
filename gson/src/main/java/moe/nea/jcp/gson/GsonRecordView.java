package moe.nea.jcp.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moe.nea.pcj.json.RecordView;

import java.util.Collection;
import java.util.Optional;

public class GsonRecordView implements RecordView<JsonElement> {
	final JsonObject jsonObject;

	public GsonRecordView(JsonObject jsonObject) {this.jsonObject = jsonObject;}

	@Override
	public Collection<String> getKeys() {
		return jsonObject.keySet();
	}

	@Override
	public Optional<JsonElement> get(String key) {
		return Optional.ofNullable(jsonObject.get(key));
	}
}
