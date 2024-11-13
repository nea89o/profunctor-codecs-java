package moe.nea.pcj;

import java.util.List;
import java.util.Optional;

public interface AppendableError<T extends AppendableError<T>> {
	/**
	 * @return an optional Self with getSuppressed containing the argument
	 */
	Optional<T> appendError(Object other);

	List<T> getSuppressed();

	static <T> T concatError(T left, T right) {
		if (left instanceof AppendableError<?> appendable) {
			var opt = (Optional<T>) appendable.appendError(right);
			return opt.orElse(left);
		}
		return left;
	}
}
