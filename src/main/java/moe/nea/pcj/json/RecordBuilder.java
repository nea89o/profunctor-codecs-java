package moe.nea.pcj.json;

import moe.nea.pcj.Result;
import moe.nea.pcj.Unit;

public interface RecordBuilder<ElementFormat> extends RecordView<ElementFormat> {
	Result<Unit, JsonLikeError> add(String key, ElementFormat value);

	Result<RecordBuilder<ElementFormat>, JsonLikeError> mergeWith(RecordBuilder<ElementFormat> other);

	ElementFormat complete();
}
