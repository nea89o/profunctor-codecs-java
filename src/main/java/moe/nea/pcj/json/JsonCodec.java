package moe.nea.pcj.json;

import moe.nea.pcj.Codec;
import moe.nea.pcj.Result;
import moe.nea.pcj.Unit;

import java.util.ArrayList;
import java.util.List;

public interface JsonCodec<T, Format> extends Codec<
		T, Format, JsonLikeOperations<Format>,
		JsonLikeError, JsonLikeError> {

	default JsonCodec<List<T>, Format> listOf() {
		return new JsonCodec<>() {
			@Override
			public Result<Format, JsonLikeError> encode(List<T> data, JsonLikeOperations<Format> ops) {
				var list = ops.createList();
				var erros = new ArrayList<JsonLikeError>();
				for (int i = 0; i < data.size(); i++) {
					var datum = data.get(i);
					final var index = i;
					var result = JsonCodec.this.encode(datum, ops)
					                           .mapError(it -> new AtIndex(index, it));
					erros.addAll(result.errors());
					result.valueOrPartial().ifPresent(list::add);
				}
				return Result.<Format, JsonLikeError>ok(list.complete()).appendErrors(erros);
			}

			@Override
			public Result<List<T>, JsonLikeError> decode(Format format, JsonLikeOperations<Format> ops) {
				var view = Result.<ListView<Format>, JsonLikeError>cast(ops.getList(format));
				return view.flatMap(elements -> {
					var acc = new ArrayList<T>(elements.length());
					var errors = new ArrayList<JsonLikeError>();
					for (int i = 0; i < elements.length(); i++) {
						final var index = i;
						var result = JsonCodec.this.decode(elements.getUnsafe(i), ops)
						                           .mapError(it -> new AtIndex(index, it));
						errors.addAll(result.errors());
						result.valueOrPartial().ifPresent(acc::add);
					}
					return Result.<List<T>, JsonLikeError>ok(acc).appendErrors(errors);
				});
			}
		};
	}

	default MapCodec<T, Format> fieldOf(String key) {
		return new MapCodec<>() {
			@Override
			public Result<T, JsonLikeError> decode(RecordView<Format> record, JsonLikeOperations<Format> ops) {
				return record.get(key)
				             .map(element -> Result.<T, JsonLikeError>cast(
						             JsonCodec.this.decode(element, ops)
						                           .mapError(it -> new AtField(key, it))))
				             .orElseGet(() -> Result.fail(new MissingKey(key)));
			}

			@Override
			public Result<RecordBuilder<Format>, JsonLikeError> encode(T value, JsonLikeOperations<Format> ops) {
				var record = ops.createObject();
				return Result.<Format, JsonLikeError>cast(JsonCodec.this.encode(value, ops))
				             .<JsonLikeError>mapError(it -> new AtField(key, it))
				             .flatMap(json -> Result.cast(record.add(key, json).map(unit -> record)));
			}

		};
	}
}
