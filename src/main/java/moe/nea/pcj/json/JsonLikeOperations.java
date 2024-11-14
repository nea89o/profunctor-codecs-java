package moe.nea.pcj.json;

import moe.nea.pcj.Operation;
import moe.nea.pcj.Result;
import moe.nea.pcj.Unit;

public interface JsonLikeOperations<Format> extends Operation<Format> {

	Format createNull(Unit value);

	Result<Unit, ? extends JsonLikeError> getNull(Format element);

	Format createNumeric(Number value);

	Result<Number, ? extends JsonLikeError> getNumeric(Format element);

	Format createString(String value);

	Result<String, ? extends JsonLikeError> getString(Format element);

	Format createBoolean(boolean value);

	Result<Boolean, ? extends JsonLikeError> getBoolean(Format format);

	RecordBuilder<Format> createObject();

	Result<? extends RecordView<Format>, ? extends JsonLikeError> getObject(Format format);

	ListBuilder<Format, Format> createList();

	Result<? extends ListView<Format>, ? extends JsonLikeError> getList(Format format);
}
