package moe.nea.pcj;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public sealed interface Result<Good, Bad> permits Result.Ok, Result.Fail {
	default boolean isOk() {
		return errors().isEmpty();
	}

	Optional<Good> value();

	Optional<Good> partial();

	default Optional<Good> valueOrPartial() {
		return value().or(this::partial);
	}

	List<Bad> errors();

	default <Next> Result<Next, Bad> map(Function<Good, Next> mapper) {
		return flatMap(mapper.andThen(Result::ok));
	}

	<Next> Result<Next, Bad> flatMap(Function<Good, Result<? extends Next, ? extends Bad>> mapper);

	default <NextBad> Result<Good, NextBad> mapError(Function<Bad, NextBad> mapper) {
		return mapErrors(it -> it.stream().map(mapper).toList());
	}

	<NextBad> Result<Good, NextBad> mapErrors(Function<List<Bad>, List<NextBad>> mapper);

	Result<Good, Bad> appendErrors(List<Bad> error);

	record Ok<Good, Bad>(Good okValue) implements Result<Good, Bad> {
		@Override
		public Result<Good, Bad> appendErrors(List<Bad> errors) {
			return new Fail<>(okValue, errors);
		}

		@Override
		public <NextBad> Result<Good, NextBad> mapErrors(Function<List<Bad>, List<NextBad>> mapper) {
			return new Ok<>(okValue);
		}

		@Override
		public Optional<Good> partial() {
			return Optional.empty();
		}

		@Override
		public List<Bad> errors() {
			return List.of();
		}

		@Override
		public <Next> Result<Next, Bad> flatMap(Function<Good, Result<? extends Next, ? extends Bad>> mapper) {
			return Result.cast(mapper.apply(okValue));
		}

		@Override
		public Optional<Good> value() {
			return Optional.of(okValue);
		}
	}

	record Fail<Good, Bad>(@Nullable Good partialValue, List<Bad> badValue) implements Result<Good, Bad> {
		public Fail {
			if (badValue.isEmpty())
				throw new IllegalArgumentException("Cannot create failure without any error values");
		}

		@Override
		public Optional<Good> value() {
			return Optional.empty();
		}

		@Override
		public Optional<Good> partial() {
			return Optional.ofNullable(partialValue);
		}

		@Override
		public List<Bad> errors() {
			return Collections.unmodifiableList(badValue);
		}

		@Override
		public <Next> Result<Next, Bad> flatMap(Function<Good, Result<? extends Next, ? extends Bad>> mapper) {
			if (partialValue != null) {
				return Result.<Next, Bad>cast(mapper.apply(partialValue)).appendErrors(badValue);
			}
			return new Fail<>(null, badValue);
		}

		@Override
		public <NextBad> Result<Good, NextBad> mapErrors(Function<List<Bad>, List<NextBad>> mapper) {
			return new Fail<>(partialValue, mapper.apply(badValue));
		}

		@Override
		public Result<Good, Bad> appendErrors(List<Bad> errors) {
			var nextErrors = new ArrayList<>(badValue);
			nextErrors.addAll(errors);
			return new Fail<>(partialValue, nextErrors);
		}
	}

	static <Good, Bad> Result<Good, Bad> ok(Good value) {
		return new Ok<>(value);
	}

	static <Good, Bad> Result.Fail<Good, Bad> fail(Bad error) {
		return new Fail<>(null, List.of(error));
	}


	static <Good, Bad> Result<Good, Bad> cast(Result<? extends Good, ? extends Bad> c) {
		//noinspection unchecked
		return (Result<Good, Bad>) c;
	}

	static <Good, Bad> Result.Fail<Good, Bad> partial(@Nullable Good partial, Bad error) {
		return new Fail<>(partial, List.of(error));
	}
}
