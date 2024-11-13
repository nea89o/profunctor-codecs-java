package moe.nea.pcj;

import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Result<Good, Bad> permits Result.Ok, Result.Fail {
	default boolean isOk() {
		return error().isEmpty();
	}

	Optional<Good> value();

	Optional<Good> partial();

	default Optional<Good> valueOrPartial() {
		return value().or(this::partial);
	}

	Optional<Bad> error();

	default <Next> Result<Next, Bad> map(Function<Good, Next> mapper) {
		return flatMap(mapper.andThen(Result::ok));
	}

	default <Next> Result<Next, Bad> flatMap(Function<Good, Result<Next, Bad>> mapper) {
		return flatMapBoth(mapper, Result::fail);
	}

	<NextGood> Result<NextGood, Bad> flatMapBoth(
			Function<Good, Result<NextGood, Bad>> mapGood,
			Function<Bad, Result<NextGood, Bad>> mapBad);

	Result<Good, Bad> appendError(Bad error);

	record Ok<Good, Bad>(Good okValue) implements Result<Good, Bad> {
		@Override
		public Result<Good, Bad> appendError(Bad error) {
			return Result.partial(okValue, error);
		}

		@Override
		public Optional<Good> partial() {
			return Optional.empty();
		}

		@Override
		public Optional<Good> value() {
			return Optional.of(okValue);
		}

		@Override
		public Optional<Bad> error() {
			return Optional.empty();
		}

		@Override
		public <NextGood> Result<NextGood, Bad> flatMapBoth(Function<Good, Result<NextGood, Bad>> mapGood, Function<Bad, Result<NextGood, Bad>> mapBad) {
			return mapGood.apply(okValue);
		}
	}

	record Fail<Good, Bad>(@Nullable Good partialValue, Bad badValue) implements Result<Good, Bad> {

		@Override
		public Optional<Good> value() {
			return Optional.empty();
		}

		@Override
		public Optional<Good> partial() {
			return Optional.ofNullable(partialValue);
		}

		@Override
		public Optional<Bad> error() {
			return Optional.of(badValue);
		}

		@Override
		public <NextGood> Result<NextGood, Bad> flatMapBoth(Function<Good, Result<NextGood, Bad>> mapGood, Function<Bad, Result<NextGood, Bad>> mapBad) {
			if (partialValue != null) {
				var nextPartial = mapGood.apply(partialValue);
				return nextPartial.appendError(badValue);
			}
			return mapBad.apply(badValue);
		}

		@Override
		public Result<Good, Bad> appendError(Bad error) {
			return Result.partial(partialValue, AppendableError.concatError(badValue, error));
		}
	}

	static <Good, Bad> Result<Good, Bad> ok(Good value) {
		return new Ok<>(value);
	}

	static <Good, Bad> Result.Fail<Good, Bad> fail(Bad error) {
		return new Fail<>(null, error);
	}

	static <Good, Bad> Result.Fail<Good, Bad> partial(@Nullable Good partial, Bad error) {
		return new Fail<>(partial, error);
	}
}
