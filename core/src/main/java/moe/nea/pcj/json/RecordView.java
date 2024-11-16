package moe.nea.pcj.json;

import java.util.Collection;
import java.util.Optional;

public interface RecordView<Format> {
	Collection<String> getKeys();

	Optional<Format> get(String key);
}
