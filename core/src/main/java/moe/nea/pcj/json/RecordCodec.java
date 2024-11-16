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
		return Result.cast(codec().decode(data, ops));
	}

	static <Format> Result<RecordBuilder<Format>, JsonLikeError> merge(Result<RecordBuilder<Format>, JsonLikeError> left, Result<RecordBuilder<Format>, JsonLikeError> right) {
		return left.flatMap(l -> right.flatMap(l::mergeWith));
	}
}
