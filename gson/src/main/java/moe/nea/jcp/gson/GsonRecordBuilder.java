package moe.nea.jcp.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moe.nea.pcj.Result;
import moe.nea.pcj.Unit;
import moe.nea.pcj.json.DuplicateJsonKey;
import moe.nea.pcj.json.JsonLikeError;
import moe.nea.pcj.json.RecordBuilder;

import java.util.stream.Stream;

public class GsonRecordBuilder extends GsonRecordView implements RecordBuilder<JsonElement> {
	public GsonRecordBuilder() {
		super(new JsonObject());
	}

	boolean completed = false;

	@Override
	public Result<RecordBuilder<JsonElement>, JsonLikeError> mergeWith(RecordBuilder<JsonElement> other) {
		var next = new GsonRecordBuilder();
		return Result.cast(Stream.of(this.complete(), other.complete())
		                         .flatMap(it -> ((JsonObject) it).entrySet().stream())
		                         .map(it -> next.add(it.getKey(), it.getValue()))
		                         .reduce((left, right) -> left.appendErrors(right.errors()))
		                         .map(it -> it.map(unit -> next))
		                         .orElse(Result.ok(next)));
	}

	@Override
	public Result<Unit, JsonLikeError> add(String key, JsonElement value) {
		if (completed) throw new IllegalStateException("JsonObject already completed");
		if (jsonObject.has(key))
			return Result.fail(new DuplicateJsonKey(key));
		jsonObject.add(key, value);
		return Result.ok(Unit.INSTANCE);
	}

	@Override
	public JsonObject complete() {
		if (completed)
			throw new IllegalStateException("JsonObject already completed");
		completed = true;
		return jsonObject;
	}
}
