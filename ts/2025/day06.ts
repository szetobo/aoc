let p1 = 0, p2 = 0
let lines: string[] = []
for await (const line of console) {
	if (line === "") { continue }
	lines.push(line)
}

function transpose<T>(matrix: T[][]): T[][] {
	return (matrix[0] === undefined) ? [] : matrix[0].map((_, j) => matrix.map(r => r[j]!))
}

let N = lines.map(line => line.split(/\s+/).filter(Boolean))
for (const col of transpose(N)) {
	const ints = col.slice(0, -1).map(Number)
	const op = col.at(-1)
	p1 += op === "+" ?
		ints.reduce((m, v) => m + v, 0) :
		ints.reduce((m, v) => m * v, 1)
}

N = transpose(lines.map(line => line.split("")))
let fs = true, OP = "", R = 0
for (const [i, col] of N.entries()) {
	const num = Number(col.slice(0, -1).join(""))
	const op = col.at(-1)!
	if (fs && op != " ") {
		OP = op
		R = OP === "+" ? 0 : 1
		fs = false
	}
	if (num === 0) {
		fs = true
	} else {
		if (OP === "+") {
			R += num
		} else {
			R *= num
		}
	}
	if (fs || i === N.length - 1) {
		p2 += R
	}
}

console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
