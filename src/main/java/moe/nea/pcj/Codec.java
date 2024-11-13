package moe.nea.pcj;

public interface Codec<Typ, Format, DeErr, CoErr> extends Decode<Typ, Format, DeErr>, Encode<Typ, Format, CoErr> {

}
