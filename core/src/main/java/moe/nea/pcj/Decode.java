package moe.nea.pcj;

public interface Decode<Typ, Format, Op extends Operation<Format>, Err> {
	Result<? extends Typ, ? extends Err> decode(Format format, Op ops);
}
