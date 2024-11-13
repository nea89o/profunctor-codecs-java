package moe.nea.pcj;

import java.util.Optional;

public interface ListView<Format> {
	int length();

	default Optional<Format> getSafe(int index) {
		if (index < 0 || index >= length()) return Optional.empty();
		return Optional.of(getUnsafe(index));
	}

	Format getUnsafe(int index);
}
