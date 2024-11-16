package moe.nea.pcj.json;

public record UnexpectedJsonElement(
		String expectedType,
		Object actualJsonObject
) implements JsonLikeError {
}
