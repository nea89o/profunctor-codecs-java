package moe.nea.pcj;

public interface Codec<Typ, Format, Op extends Operation<Format>, DeErr, CoErr>
		extends Decode<Typ, Format, Op, DeErr>, Encode<Typ, Format, Op, CoErr> {

}
