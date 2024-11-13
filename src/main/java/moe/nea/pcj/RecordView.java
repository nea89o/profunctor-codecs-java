package moe.nea.pcj;

import java.util.Collection;
import java.util.Optional;

public interface RecordView<Format> {
	Collection<Format> getKeys();

	Optional<Format> get(String key);
}
