let p1 = 0, p2 = 0

const lines = (await Bun.stdin.text()).trim().split("\n").map(line => line.split(""))
let ways = new Map<string, number>()

const n = lines.length
for (const [row, line] of lines.slice(0, -1).entries()) {
	for (const [col, ch] of line.entries()) {
		if (ch === "|" || ch === "S") {
			let nextRow = lines[row + 1]
			if (nextRow === undefined) { continue }
			let cnt = ways.get(`${row},${col}`) ?? 1
			const char = nextRow[col]
			if (char === "^") {
				nextRow[col - 1] = "|"
				ways.set(`${row + 1},${col - 1}`, cnt + (ways.get(`${row + 1},${col - 1}`) ?? 0))
				nextRow[col + 1] = "|"
				ways.set(`${row + 1},${col + 1}`, cnt + (ways.get(`${row + 1},${col + 1}`) ?? 0))
				p1++
			}
			if (char === "." || char === "|") {
				nextRow[col] = "|"
				ways.set(`${row + 1},${col}`, cnt + (ways.get(`${row + 1},${col}`) ?? 0))
			}
		}
	}
}
for (const [col, ch] of lines.at(n - 1)!.entries()) {
	if (ch === "|") { p2 += (ways.get(`${n - 1},${col}`) ?? 0) }
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
