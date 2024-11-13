package moe.nea.pcj;

public interface JsonLikeOperations<Format> extends Operation<Format> {

	Format createNull(Unit value);

	Result<Unit, ? extends JsonLikeError> getNull(Format element);

	Format createNumeric(Number value);

	Result<Number, ? extends JsonLikeError> getInt(Format element);

	Format createString(String value);

	Result<String, ? extends JsonLikeError> getString(Format element);

	Format createBoolean(boolean value);

	Result<Boolean, ? extends JsonLikeError> getBoolean(Format format);

	RecordBuilder<? extends Format, Format> createObject();

	Result<RecordView<? extends Format>, ? extends JsonLikeError> getObject(Format format);

	ListBuilder<? extends Format, Format> createList();

	Result<ListView<? extends Format>, ? extends JsonLikeError> getList(Format format);
}
