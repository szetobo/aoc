type point = [row: number, col: number]

const adjacent8 = (c: point, limit: number): point[] => {
	const offset: point[] = [
		[-1, -1], [-1, 0], [-1, 1],
		[0, -1], [0, 1],
		[1, -1], [1, 0], [1, 1],
	]
	let adj: point[] = []
	for (const pt of offset.map(([row, col]) => [c[0] + row, c[1] + col] as point)) {
		if (pt.every(v => v >= 0 && v < limit)) {
			adj.push(pt)
		}
	}
	return adj
}

let part1 = 0, part2 = 0
let inputs: string[][] = []
for await (const line of console) {
	if (line === "") { continue }
	inputs.push(line.split(""))
}
for (let i = 0; ; i++) {
	let done = part2
	for (const [row, rows] of inputs.entries()) {
		for (const [col, ch] of rows.entries()) {
			if (ch === "@") {
				const cnt = adjacent8([row, col], inputs.length).filter(([r, c]) => inputs[r]?.[c] === "@").length
				if (cnt < 4) {
					if (i === 0) {
						part1++
					} else {
						rows[col] = "x"
						part2++
					}
				}
			}
		}
	}
	if (i > 0 && done === part2) {
		break
	}
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
