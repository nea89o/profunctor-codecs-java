package moe.nea.pcj;

public interface Encode<Typ, Format, Op extends Operation<Format>, Err> {
	Result<? extends Format, ? extends Err> encode(Typ data, Op ops);
}
