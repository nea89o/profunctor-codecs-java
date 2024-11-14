package moe.nea.jcp.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import moe.nea.pcj.json.JsonLikeError;
import moe.nea.pcj.json.JsonLikeOperations;
import moe.nea.pcj.json.ListBuilder;
import moe.nea.pcj.json.ListView;
import moe.nea.pcj.json.RecordBuilder;
import moe.nea.pcj.json.RecordView;
import moe.nea.pcj.Result;
import moe.nea.pcj.Unit;
import moe.nea.pcj.json.UnexpectedJsonElement;

public class GsonOperations implements JsonLikeOperations<JsonElement> {
	@Override
	public JsonElement createNull(Unit value) {
		return JsonNull.INSTANCE;
	}

	@Override
	public Result<Unit, ? extends JsonLikeError> getNull(JsonElement element) {
		if (element.isJsonNull()) return Result.ok(Unit.INSTANCE);
		return Result.fail(new UnexpectedJsonElement("null", element));
	}

	@Override
	public JsonElement createNumeric(Number value) {
		return new JsonPrimitive(value);
	}

	@Override
	public Result<Number, ? extends JsonLikeError> getNumeric(JsonElement element) {
		if (element instanceof JsonPrimitive primitive && primitive.isNumber()) {
			return Result.ok(primitive.getAsNumber());
		}
		return Result.fail(new UnexpectedJsonElement("number", element));
	}

	@Override
	public JsonElement createString(String value) {
		return new JsonPrimitive(value);
	}

	@Override
	public Result<String, ? extends JsonLikeError> getString(JsonElement element) {
		if (element instanceof JsonPrimitive primitive && primitive.isString()) {
			return Result.ok(primitive.getAsString());
		}
		return Result.fail(new UnexpectedJsonElement("string", element));
	}

	@Override
	public JsonElement createBoolean(boolean value) {
		return new JsonPrimitive(value);
	}

	@Override
	public Result<Boolean, ? extends JsonLikeError> getBoolean(JsonElement jsonElement) {
		if (jsonElement instanceof JsonPrimitive primitive && primitive.isBoolean()) {
			return Result.ok(primitive.getAsBoolean());
		}
		return Result.fail(new UnexpectedJsonElement("boolean", jsonElement));
	}

	@Override
	public RecordBuilder<JsonElement> createObject() {
		return new GsonRecordBuilder();
	}

	@Override
	public Result<RecordView<JsonElement>, ? extends JsonLikeError> getObject(JsonElement jsonElement) {
		if (jsonElement instanceof JsonObject object) {
			return Result.ok(new GsonRecordView(object));
		}
		return Result.fail(new UnexpectedJsonElement("object", jsonElement));
	}

	@Override
	public ListBuilder<JsonArray, JsonElement> createList() {
		return new GsonListBuilder();
	}

	@Override
	public Result<ListView<JsonElement>, ? extends JsonLikeError> getList(JsonElement jsonElement) {
		if (jsonElement instanceof JsonArray array) {
			return Result.ok(new GsonListView(array));
		}
		return Result.fail(new UnexpectedJsonElement("list", jsonElement));
	}
}
