package moe.nea.pcj.json;

import java.util.Set;

public record UnknownSubtype<T>(T actual, Set<T> expectedTypes) implements JsonLikeError {
	@SafeVarargs
	public UnknownSubtype(T actual, T... expected) {
		this(actual, Set.of(expected));
	}
}
