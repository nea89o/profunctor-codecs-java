package moe.nea.pcj.json;

public interface ListBuilder<Format, ElementFormat> extends ListView<ElementFormat> {
	Format complete();

	void add(ElementFormat value);

	void set(int index, ElementFormat value);
}
