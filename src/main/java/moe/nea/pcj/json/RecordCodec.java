package moe.nea.pcj.json;

import moe.nea.pcj.Result;

import java.util.function.Function;

public record RecordCodec<O, T, Format>(
		MapCodec<T, Format> codec,
		Function<O, T> getter
) {

	Result<RecordBuilder<Format>, JsonLikeError> enc(O data, JsonLikeOperations<Format> ops) {
		return codec().encode(getter().apply(data), ops);
	}

	Result<T, JsonLikeError> dec(RecordView<Format> data, JsonLikeOperations<Format> ops) {
		return codec().decode(data, ops);
	}

	static <Format> Result<RecordBuilder<Format>, JsonLikeError> merge(Result<RecordBuilder<Format>, JsonLikeError> left, Result<RecordBuilder<Format>, JsonLikeError> right) {
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

}
