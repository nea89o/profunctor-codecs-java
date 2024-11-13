package moe.nea.jcp.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import moe.nea.pcj.JsonLikeError;
import moe.nea.pcj.JsonLikeOperations;
import moe.nea.pcj.ListBuilder;
import moe.nea.pcj.ListView;
import moe.nea.pcj.RecordBuilder;
import moe.nea.pcj.RecordView;
import moe.nea.pcj.Result;
import moe.nea.pcj.Unit;

public class GsonOperations implements JsonLikeOperations<JsonElement> {
	@Override
	public JsonElement createNull(Unit value) {
		return JsonNull.INSTANCE;
	}

	@Override
	public Result<Unit, ? extends JsonLikeError> getNull(JsonElement element) {
		if (element.isJsonNull()) return Result.ok(Unit.INSTANCE);
		return Result.fail();
	}

	@Override
	public JsonElement createNumeric(Number value) {
		return null;
	}

	@Override
	public Result<Number, ? extends JsonLikeError> getInt(JsonElement element) {
		return null;
	}

	@Override
	public JsonElement createString(String value) {
		return null;
	}

	@Override
	public Result<String, ? extends JsonLikeError> getString(JsonElement element) {
		return null;
	}

	@Override
	public JsonElement createBoolean(boolean value) {
		return null;
	}

	@Override
	public Result<Boolean, ? extends JsonLikeError> getBoolean(JsonElement jsonElement) {
		return null;
	}

	@Override
	public RecordBuilder<? extends JsonElement, JsonElement> createObject() {
		return null;
	}

	@Override
	public Result<RecordView<? extends JsonElement>, ? extends JsonLikeError> getObject(JsonElement jsonElement) {
		return null;
	}

	@Override
	public ListBuilder<? extends JsonElement, JsonElement> createList() {
		return null;
	}

	@Override
	public Result<ListView<? extends JsonElement>, ? extends JsonLikeError> getList(JsonElement jsonElement) {
		return null;
	}
}
