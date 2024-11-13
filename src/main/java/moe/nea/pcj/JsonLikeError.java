package moe.nea.pcj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class JsonLikeError<Self extends JsonLikeError<?>> implements AppendableError<JsonLikeError<?>> {
	private final List<JsonLikeError<?>> suppressed = new ArrayList<>();

	protected abstract Self copySelfWithoutSuppressions();

	@Override
	public List<JsonLikeError<?>> getSuppressed() {
		return Collections.unmodifiableList(suppressed);
	}

	@Override
	public Optional<JsonLikeError<?>> appendError(Object other) {
		if (other instanceof JsonLikeError<?> jsonLikeOther) {
			var newSelf = (JsonLikeError<?>) copySelfWithoutSuppressions();
			newSelf.suppressed.add(jsonLikeOther.copySelfWithoutSuppressions());
			newSelf.suppressed.addAll(jsonLikeOther.getSuppressed());
			return Optional.of(newSelf);
		} else {
			return Optional.empty();
		}
	}
}
