package moe.nea.pcj.json;

import moe.nea.pcj.Result;

import java.util.function.Function;

public interface MapCodec<T, Format> {
	Result<T, JsonLikeError> decode(
			RecordView<Format> record,
			JsonLikeOperations<Format> ops);

	Result<RecordBuilder<Format>, JsonLikeError> encode(T value, JsonLikeOperations<Format> ops);

	default <O> RecordCodec<O, T, Format> withGetter(Function<O, T> getter) {
		return new RecordCodec<>(this, getter);
	}
}
