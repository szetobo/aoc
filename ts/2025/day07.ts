let part1 = 0, part2 = 0
let inputs: string[][] = [], ways = new Map<string, number>()
for await (const line of console) {
	if (line === "") continue
	inputs.push(line.split(""))
}
inputs.slice(0, -1).forEach((str, row) => {
	str.forEach((ch, col) => {
		if (ch === "|" || ch === "S") {
			let cnt = ways.get(`${row},${col}`) ?? 1
			ch = inputs[row + 1]![col]!
			if (ch === "^") {
				inputs[row + 1]![col - 1] = "|"
				ways.set(`${row + 1},${col - 1}`, cnt + (ways.get(`${row + 1},${col - 1}`) ?? 0))
				inputs[row + 1]![col + 1] = "|"
				ways.set(`${row + 1},${col + 1}`, cnt + (ways.get(`${row + 1},${col + 1}`) ?? 0))
				part1++
			}
			if (ch === "." || ch === "|") {
				inputs[row + 1]![col] = "|"
				ways.set(`${row + 1},${col}`, cnt + (ways.get(`${row + 1},${col}`) ?? 0))
			}
		}
	})
})
const lastRow = inputs.length - 1
inputs.at(lastRow)?.forEach((ch, col) => {
	if (ch === "|") {
		part2 += (ways.get(`${lastRow},${col}`) ?? 0)
	}
})
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
