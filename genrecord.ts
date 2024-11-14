#!/usr/bin/env node --experimental-strip-types
function typArgs(a: string[]): string {
    if (a.length == 0) return ""
    return "<" + a.join(", ") + ">"
}

function argFor(va: string): string {
    return va.replace("T", "arg")
}

/*	public static <T1, T2, O, Format> JsonCodec<O, Format> join(
			RecordCodec<O, T1, Format> arg1,
			RecordCodec<O, T2, Format> arg2,
			Tuple.Func2<O, T1, T2> joiner
	) {
		return new RecordCompleteCodec<>() {

			@Override
			public Result<Format, JsonLikeError> encode(O data, JsonLikeOperations<Format> ops) {
				return Stream.of(arg1.enc(data, ops), arg2.enc(data, ops))
				             .reduce(Result.ok(ops.createObject()), RecordCodec::merge)
				             .map(RecordBuilder::complete);
			}

			@Override
			public Result<O, JsonLikeError> decode(RecordView<Format> format, JsonLikeOperations<Format> ops) {
				return Tuple.Tuple2.collect(new Tuple.Tuple2<>(arg1, arg2)
						                            .map(it -> it.dec(format, ops), it -> it.dec(format, ops)))
				                   .map(it -> it.applyTo(joiner));
			}
		};
	}
*/

function genRecordJoiner(elements: number) {
    if (!elements) return
    const vars = [...new Array(elements)].map((_, idx) => "T" + idx)
    console.log(`\tpublic static ${typArgs([...vars, 'O', 'Format'])} JsonCodec<O, Format> join(`)
    for (let var1 of vars) {
        console.log(`\t\tRecordCodec<O, ${var1}, Format> ${argFor(var1)},`)
    }
    console.log(`\t\tTuple.Func${elements}${typArgs(['O', ...vars])} joiner`)
    console.log("\t) {")
    console.log("\t\treturn new RecordCompleteCodec<>() {")

    console.log("\t\t\t@Override")
    console.log("\t\t\tpublic Result<Format, JsonLikeError> encode(O data, JsonLikeOperations<Format> ops) {")
    console.log(`\t\t\t\treturn Stream.of(${vars.map(it => argFor(it) + ".enc(data, ops)").join(", ")})`)
    console.log(`\t\t\t\t\t.reduce(Result.ok(ops.createObject()), RecordCodec::merge)`)
    console.log(`\t\t\t\t\t.map(RecordBuilder::complete);`)
    console.log("\t\t\t}")

    console.log("\t\t\t@Override")
    console.log("\t\t\tpublic Result<O, JsonLikeError> decode(RecordView<Format> format, JsonLikeOperations<Format> ops) {")
    console.log(`\t\t\t\treturn Tuple.Tuple${elements}.collect(new Tuple.Tuple${elements}${elements ? '<>' : ''}(${vars.map(it => argFor(it) + ".dec(format, ops)").join(", ")}))`)
    console.log("\t\t\t\t\t.map(it -> it.applyTo(joiner));")
    console.log("\t\t\t}")

    console.log("\t\t};")
    console.log("\t}")
}

function genRecords(maxI: number) {
    console.log("// @gen" + "erated by genrecord.ts")
    console.log("package moe.nea.pcj.json;")
    console.log(`
import moe.nea.pcj.*;
import moe.nea.pcj.json.RecordCodec.*;
import java.util.stream.*;`)
    console.log()
    console.log("@SuppressWarnings(\"unused\")")
    console.log("public class RecordJoiners {")
    for (let i = 0; i < maxI; i++) {
        genRecordJoiner(i)
    }
    console.log("}")
}

genRecords(15)

