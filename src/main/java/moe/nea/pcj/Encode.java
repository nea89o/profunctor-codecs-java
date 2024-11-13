package moe.nea.pcj;

public interface Encode<Typ, Format, Err> {
	Result<Format, Err> encode(Typ data, Operation<Format> op);
}
