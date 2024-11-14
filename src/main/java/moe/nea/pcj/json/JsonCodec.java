package moe.nea.pcj.json;

import moe.nea.pcj.Codec;
import moe.nea.pcj.Result;

public interface JsonCodec<T, Format> extends Codec<
		T, Format, JsonLikeOperations<Format>,
		JsonLikeError, JsonLikeError> {

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
