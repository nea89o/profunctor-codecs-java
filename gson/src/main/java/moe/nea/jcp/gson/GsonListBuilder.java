package moe.nea.jcp.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import moe.nea.pcj.json.ListBuilder;

public class GsonListBuilder extends GsonListView implements ListBuilder<JsonArray, JsonElement> {

	public GsonListBuilder() {
		super(new JsonArray());
	}

	@Override
	public JsonArray complete() {
		return jsonArray;
	}

	@Override
	public void add(JsonElement value) {
		jsonArray.add(value);
	}

	@Override
	public void set(int index, JsonElement value) {
		jsonArray.set(index, value);
	}
}
