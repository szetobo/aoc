let part1 = 0, part2 = 0
let inputs: number[][] = [], ops: string[] = [], chars: string[][] = []
for await (const line of console) {
	if (line === "") { continue }
	chars.push(line.split(""))
	if (line[0] === "+" || line[0] === "*") {
		ops = line.split(/\s+/)
	} else {
		inputs.push(line.split(/\s+/).map(Number))
	}
}

for (const [i, op] of ops.entries()) {
	part1 += (op === "+") ?
		inputs.reduce((m, v) => m + (v[i] ?? 0), 0) :
		inputs.reduce((m, v) => m * (v[i] ?? 0), 1)
}

chars = chars[0]!.map((_, i) => chars.map(r => r[i]!))

let fs = true, op = "", res = 0
for (const [i, row] of chars.entries()) {
	if (fs) {
		res = (op = row.at(-1)!) === "+" ? 0 : 1
		fs = !fs
	}
	const str = row.slice(0, -1).join("")
	if (str === "    ") {
		fs = true
	} else {
		let val = Number(str)
		if (op === "+") {
			res += val
		} else {
			res *= val
		}
	}
	if (fs || i == chars.length - 1) {
		part2 += res
	}

}

console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
