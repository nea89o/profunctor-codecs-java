package moe.nea.pcj;

public interface ListBuilder<Format, ElementFormat> extends ListView<ElementFormat> {
	Format complete();

	void add(ElementFormat value);

	void set(int index, ElementFormat value);
}
