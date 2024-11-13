package moe.nea.pcj;

public interface Decode<Typ, Format, Err> {
	Result<Typ, Err> decode(Format format, Operation<Format> op);
}
