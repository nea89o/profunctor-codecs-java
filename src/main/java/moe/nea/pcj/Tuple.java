package moe.nea.pcj;

public interface Tuple {
	record Tuple2<A, B>(A first, B second) implements Tuple {}

	record Tuple3<A, B, C>(A first, B second, C third) implements Tuple {}

	record Tuple4<A, B, C, D>(A first, B second, C third, D fourth) implements Tuple {}

	record Tuple5<A, B, C, D, E>(A first, B second, C third, D fourth, E fifth) implements Tuple {}

	record Tuple6<A, B, C, D, E, F>(A first, B second, C third, D fourth, E fifth, F sixth) implements Tuple {}

	record Tuple7<A, B, C, D, E, F, G>(A first, B second, C third, D fourth, E fifth, F sixth,
	                                   G seventh) implements Tuple {}
}
