package moe.nea.jcp.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import moe.nea.pcj.json.ListView;

public class GsonListView implements ListView<JsonElement> {
	final JsonArray jsonArray;

	public GsonListView(JsonArray jsonArray) {this.jsonArray = jsonArray;}

	@Override
	public int length() {
		return jsonArray.size();
	}

	@Override
	public JsonElement getUnsafe(int index) {
		return jsonArray.get(index);
	}
}
