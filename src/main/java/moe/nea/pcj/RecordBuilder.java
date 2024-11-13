package moe.nea.pcj;

public interface RecordBuilder<Format, ElementFormat> extends RecordView<ElementFormat> {
	void add(String key, ElementFormat value); // TODO
	Format complete();
}
