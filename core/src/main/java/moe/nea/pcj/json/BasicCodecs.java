package moe.nea.pcj.json;

import moe.nea.pcj.Result;

public class BasicCodecs<Format> {
	protected BasicCodecs() {}

	public static <Format> BasicCodecs<Format> create() {
		return new BasicCodecs<>();
	}

	public final JsonCodec<String, Format> STRING = new JsonCodec<>() {
		@Override
		public Result<String, ? extends JsonLikeError> decode(Format format, JsonLikeOperations<Format> ops) {
			return ops.getString(format);
		}

		@Override
		public Result<Format, ? extends JsonLikeError> encode(String data, JsonLikeOperations<Format> ops) {
			return Result.ok(ops.createString(data));
		}
	};

	public final JsonCodec<Float, Format> FLOAT = new JsonCodec<Float, Format>() {
		@Override
		public Result<? extends Float, ? extends JsonLikeError> decode(Format format, JsonLikeOperations<Format> ops) {
			return ops.getNumeric(format).map(Number::floatValue);
		}

		@Override
		public Result<? extends Format, ? extends JsonLikeError> encode(Float data, JsonLikeOperations<Format> ops) {
			return Result.ok(ops.createNumeric(data));
		}
	};

	public final JsonCodec<Integer, Format> INTEGER = new JsonCodec<>() {
		@Override
		public Result<? extends Integer, ? extends JsonLikeError> decode(Format format, JsonLikeOperations<Format> ops) {
			return ops.getNumeric(format).map(Number::intValue); // TODO: filter for valid ints
		}

		@Override
		public Result<? extends Format, ? extends JsonLikeError> encode(Integer data, JsonLikeOperations<Format> ops) {
			return Result.ok(ops.createNumeric(data));
		}
	};
}