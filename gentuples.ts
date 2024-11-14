#!/usr/bin/env node --experimental-strip-types

function nameFor(va: string): string {
    return va.replace("E", "T").replace("T", "element")
}

function typArgs(a: string[]): string {
    if (a.length == 0) return ""
    return "<" + a.join(", ") + ">"
}

function genFunc(elementNumber: number, max: number) {
    console.log("\t@FunctionalInterface")
    const vars: string[] = [...new Array(elementNumber)].map((_, idx) => `T${idx}`)
    console.log(`\tinterface Func${elementNumber}${typArgs(['R', ...vars])} {`)
    console.log(`\t\tR apply(${vars.map(it => it + " " + it.replace("T", "arg")).join(", ")});`)
    console.log("\t}")
}

function genTuple(elementNumber: number, max: number) {
    const vars: string[] = [...new Array(elementNumber)].map((_, idx) => `T${idx}`)
    if (elementNumber == 0) {
        console.log("\tclass Tuple0 implements Tuple {")
    } else {
        console.log(`\trecord Tuple${elementNumber}${typArgs(vars)}(${vars.map(it => it + " " + nameFor(it)).join(", ")}) {`)
    }
    console.log(`\t\tpublic <R> R applyTo(Func${elementNumber}${typArgs(['R', ...vars])} func) {`)
    console.log(`\t\t\treturn func.apply(${vars.map(it => "this." + nameFor(it)).join(", ")});`)
    console.log("\t\t}")
    console.log(`\t\tpublic static ${typArgs(['R', ...vars])} Result<Tuple${elementNumber}${typArgs(vars)}, R> collect(`
        + `Tuple${elementNumber}${typArgs(vars.map(it => `Result<${it}, R>`))} tuple`
        + `) {`)
    console.log("\t\t\treturn")
    for (const tVar of vars) {
        console.log(`\t\t\t\ttuple.${nameFor(tVar)}.flatMap(${nameFor(tVar)} ->`)
    }
    console.log(`\t\t\t\t\tResult.ok(new Tuple${elementNumber}${elementNumber ? '<>' : ''}(${vars.map(it => nameFor(it)).join(", ")}))`)
    for (const tVar of vars) {
        console.log(`\t\t\t\t)`)
    }
    console.log("\t\t\t;")
    console.log("\t\t}")


    const newVars = vars.map(it => it.replace("T", "E"))
    console.log(`\t\tpublic ${typArgs(newVars)} Tuple${elementNumber}${typArgs(newVars)} map(`
        + newVars.map(it => `Func1<${it}, ${it.replace("E", "T")}> ${it.replace("E", "map")}`).join(", ")
        + `) {`)
    console.log(`\t\t\treturn new Tuple${elementNumber}${(elementNumber ? '<>' : '')}(${newVars.map(it => `${it.replace("E", "map")}.apply(this.${nameFor(it)})`).join(", ")});`)
    console.log("\t\t}")

    for (let extraElements = 0; extraElements < (max - elementNumber); extraElements++) {
        const extraVars = [...new Array(extraElements)].map((_, idx) => `E${idx}`)
        const sumElements = extraElements + elementNumber;
        console.log(`\t\tpublic ${typArgs(extraVars)} Tuple${sumElements}${typArgs([...vars, ...extraVars])} `
            + `join(Tuple${extraElements}${typArgs(extraVars)} other) {`)
        console.log(`\t\t\treturn new Tuple${sumElements}${sumElements ? '<>' : ''}(${[
            ...vars.map(it => "this." + nameFor(it)),
            ...extraVars.map(it => "other." + nameFor(it))].join(", ")});`)
        console.log("\t\t}")
    }
    console.log("\t}")
}


function genTuples(maxI: number) {
    console.log("// @gen" + "erated by gentuples.ts")
    console.log("package moe.nea.pcj;")
    console.log()
    console.log("@SuppressWarnings(\"unused\")")
    console.log("public interface Tuple {")
    for (let i = 0; i < maxI; i++) {
        genTuple(i, maxI);
        genFunc(i, maxI)
    }
    console.log("}")
}

genTuples(15)

