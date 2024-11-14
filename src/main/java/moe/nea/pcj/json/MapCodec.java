package moe.nea.pcj.json;

import moe.nea.pcj.Result;

import java.util.function.Function;

public interface MapCodec<T, Format> {
	Result<? extends T, JsonLikeError> decode(
			RecordView<Format> record,
			JsonLikeOperations<Format> ops);

	Result<RecordBuilder<Format>, JsonLikeError> encode(T value, JsonLikeOperations<Format> ops);

	default <O> MapCodec<? extends O, Format> dispatch(
			Function<? super O, ? extends T> keyExtractor,
			Function<? super T, MapCodec<? extends O, Format>> codecGenerator
	) {
		// TODO: the codecGenerator function is not exactly typesafe. there should be some limit on keyExtractor and codecGenerator working in tandem
		return new MapCodec<>() {
			@Override
			public Result<O, JsonLikeError> decode(RecordView<Format> record, JsonLikeOperations<Format> ops) {
				return MapCodec.this.decode(record, ops)
				                    .map(codecGenerator::apply)
				                    .flatMap(codec -> codec.decode(record, ops));
			}

			@Override
			public Result<RecordBuilder<Format>, JsonLikeError> encode(O value, JsonLikeOperations<Format> ops) {
				var key = keyExtractor.apply(value);
				var codec = codecGenerator.apply(key);
				return MapCodec.this
						.encode(key, ops)
						.flatMap(keyEncoded -> ((MapCodec<O, Format>) codec).encode(value, ops).flatMap(keyEncoded::mergeWith));
			}
		};
	}

	default JsonCodec<T, Format> codec() {
		return new JsonCodec<>() {
			@Override
			public Result<? extends T, ? extends JsonLikeError> decode(Format format, JsonLikeOperations<Format> ops) {
				return Result.<RecordView<Format>, JsonLikeError>cast(ops.getObject(format))
				             .flatMap(record -> MapCodec.this.decode(record, ops));
			}

			@Override
			public Result<? extends Format, ? extends JsonLikeError> encode(T data, JsonLikeOperations<Format> ops) {
				return Result.cast(MapCodec.this.encode(data, ops)).map(RecordBuilder::complete);
			}
		};
	}

	default <O> RecordCodec<O, T, Format> withGetter(Function<O, T> getter) {
		return new RecordCodec<>(this, getter);
	}
}
