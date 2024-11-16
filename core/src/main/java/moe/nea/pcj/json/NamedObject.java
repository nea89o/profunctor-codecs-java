package moe.nea.pcj.json;

public record NamedObject(String name, JsonLikeError error) implements JsonLikeError {
}
