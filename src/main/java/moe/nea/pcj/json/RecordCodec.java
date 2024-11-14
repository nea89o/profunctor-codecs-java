package moe.nea.pcj.json;

import moe.nea.pcj.Codec;
import moe.nea.pcj.Result;
import moe.nea.pcj.Tuple;

import java.util.function.Function;
import java.util.stream.Stream;

public record RecordCodec<O, T, Format>(
		MapCodec<T, Format> codec,
		Function<O, T> getter
) {

	private Result<RecordBuilder<Format>, JsonLikeError> enc(O data, JsonLikeOperations<Format> ops) {
		return codec().encode(getter().apply(data), ops);
	}

	private Result<T, JsonLikeError> dec(RecordView<Format> data, JsonLikeOperations<Format> ops) {
		return codec().decode(data, ops);
	}

	private static <Format> Result<RecordBuilder<Format>, JsonLikeError> merge(Result<RecordBuilder<Format>, JsonLikeError> left, Result<RecordBuilder<Format>, JsonLikeError> right) {
		return left.flatMap(l -> right.flatMap(l::mergeWith));
	}

	abstract static class RecordCompleteCodec<O, Format> implements JsonCodec<O, Format> {
		@Override
		public Result<O, JsonLikeError> decode(Format format, JsonLikeOperations<Format> ops) {
			return Result.<RecordView<Format>, JsonLikeError>cast(ops.getObject(format))
			             .flatMap(record -> (decode(record, ops)));
		}

		protected abstract Result<O, JsonLikeError> decode(RecordView<Format> record, JsonLikeOperations<Format> ops);
	}

	public static <T1, T2, O, Format> JsonCodec<O, Format> join(
			RecordCodec<O, T1, Format> arg1,
			RecordCodec<O, T2, Format> arg2,
			Tuple.Func2<O, T1, T2> joiner
	) {
		return new RecordCompleteCodec<>() {

			@Override
			public Result<Format, JsonLikeError> encode(O data, JsonLikeOperations<Format> ops) {
				return Stream.of(arg1.enc(data, ops), arg2.enc(data, ops))
				             .reduce(Result.ok(ops.createObject()), RecordCodec::merge)
				             .map(RecordBuilder::complete);
			}

			@Override
			public Result<O, JsonLikeError> decode(RecordView<Format> format, JsonLikeOperations<Format> ops) {
				return Tuple.Tuple2.collect(new Tuple.Tuple2<>(arg1, arg2)
						                            .map(it -> it.dec(format, ops), it -> it.dec(format, ops)))
				                   .map(it -> it.applyTo(joiner));
			}
		};
	}
}
