package moe.nea.pcj.json;

public interface ListBuilder<ElementFormat> extends ListView<ElementFormat> {
	ElementFormat complete();

	void add(ElementFormat value);

	void set(int index, ElementFormat value);
}
