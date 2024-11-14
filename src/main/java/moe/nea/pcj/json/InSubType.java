package moe.nea.pcj.json;

public record InSubType<T>(T typeTag, JsonLikeError error) implements JsonLikeError {
}
