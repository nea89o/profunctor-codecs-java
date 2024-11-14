package moe.nea.jcp.gson.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import moe.nea.jcp.gson.GsonCodecs;
import moe.nea.jcp.gson.GsonOperations;
import moe.nea.pcj.Codec;
import moe.nea.pcj.Decode;
import moe.nea.pcj.Result;
import moe.nea.pcj.json.AtField;
import moe.nea.pcj.json.AtIndex;
import moe.nea.pcj.json.DuplicateJsonKey;
import moe.nea.pcj.json.JsonLikeError;
import moe.nea.pcj.json.JsonLikeOperations;
import moe.nea.pcj.json.MissingKey;
import moe.nea.pcj.json.NamedObject;
import moe.nea.pcj.json.RecordJoiners;
import moe.nea.pcj.json.UnexpectedJsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TestBasic {
	private <T> void assertFail(Result<T, ?> result, JsonLikeError... expectedError) {
		if (result.isOk())
			throw new AssertionError("Expected fail");
		Assertions.assertEquals(
				Arrays.stream(expectedError).collect(Collectors.toSet()),
				new HashSet<>(result.errors())
		);
	}

	private <T> void assertSuccess(Result<T, ?> result, T expected) {
		for (Object error : result.errors()) {
			throw new AssertionError(error.toString());
		}
		Assertions.assertEquals(expected, result.valueOrPartial().get());
	}

	<T> Result<T, JsonLikeError> decode(Codec<T, JsonElement, JsonLikeOperations<JsonElement>, JsonLikeError, JsonLikeError> decode, JsonElement element) {
		Result<T, JsonLikeError> result = Result.cast(decode.decode(element, GsonOperations.INSTANCE));
		result.value().ifPresent(decoded -> Assertions.assertEquals(Result.ok(element), decode.encode(decoded, GsonOperations.INSTANCE)));
		return result;
	}

	static JsonElement mkPrim(Object arg) {
		if (arg instanceof JsonElement el) return el;
		if (arg instanceof String str) return new JsonPrimitive(str);
		if (arg instanceof Number num) return new JsonPrimitive(num);
		if (arg == null) return JsonNull.INSTANCE;
		if (arg instanceof Boolean b) return new JsonPrimitive(b);
		throw new IllegalArgumentException("Cannot convert " + arg + " to json object");
	}

	static JsonArray mkJsonArray(Object... args) {
		JsonArray array = new JsonArray();
		for (Object arg : args) {
			array.add(mkPrim(arg));
		}
		return array;
	}

	static JsonObject mkJsonObject(Object... args) {
		JsonObject obj = new JsonObject();
		for (int i = 0; i < args.length; i += 2) {
			obj.add((String) args[i], mkPrim(args[i + 1]));
		}
		return obj;
	}

	GsonCodecs codecs = GsonCodecs.INSTANCE;

	@Test
	void testString() {
		assertSuccess(decode(codecs.STRING, new JsonPrimitive("test")), "test");
	}

	@Test
	void testInt() {
		assertSuccess(decode(codecs.INTEGER, new JsonPrimitive(1000)), 1000);
		assertFail(decode(codecs.INTEGER, new JsonPrimitive("hehehe")),
		           new UnexpectedJsonElement("number", mkPrim("hehehe")));
		assertFail(decode(codecs.INTEGER, new JsonPrimitive("1")),
		           new UnexpectedJsonElement("number", mkPrim("1")));
	}

	record TestObject(
			String foo,
			int bar
	) {}


	@Test
	void testNamedFunction() {
		assertFail(decode(codecs.STRING.named("Test"), mkPrim(0)),
		           new NamedObject("Test", new UnexpectedJsonElement("string", mkPrim(0))));
	}

	@Test
	void testObject() {
		var codec = RecordJoiners.join(
				codecs.STRING.fieldOf("foo").withGetter(TestObject::foo),
				codecs.INTEGER.fieldOf("bar").withGetter(TestObject::bar),
				TestObject::new
		);
		assertSuccess(decode(codec, mkJsonObject("foo", "fooValue", "bar", -10)),
		              new TestObject("fooValue", -10));
		assertFail(decode(codec, mkJsonObject("foo", "fooValue")),
		           new MissingKey("bar"));
		assertFail(decode(codec, mkJsonObject("foo", "fooValue", "bar", "test")),
		           new AtField("bar", new UnexpectedJsonElement("number", mkPrim("test"))));
	}

	@Test
	void testDuplicateKeys() {
		var codec = RecordJoiners.join(
				codecs.STRING.fieldOf("foo").withGetter(TestObject::foo),
				codecs.INTEGER.fieldOf("foo").withGetter(TestObject::bar),
				TestObject::new
		);
		// TODO: add test for decoding with duplicate keys warning (esp. optional fields)
		assertFail(codec.encode(new TestObject("", 0), GsonOperations.INSTANCE),
		           new DuplicateJsonKey("foo"));
	}

	@Test
	void testList() {
		var codec = codecs.STRING.fieldOf("hello").codec().listOf();
		assertSuccess(decode(codec, mkJsonArray(mkJsonObject("hello", "foo"), mkJsonObject("hello", "bar"))),
		              List.of("foo", "bar"));
		assertFail(decode(codec, mkJsonArray("foo", mkJsonObject("hello", "bar"))),
		           new AtIndex(0, new UnexpectedJsonElement("object", mkPrim("foo"))));
	}
}

