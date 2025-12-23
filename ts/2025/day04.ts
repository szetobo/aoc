interface cell {
	row: number
	col: number
}
const adjacent8 = (c: cell, limit: number): cell[] => {
	const offset = [
		[-1, -1], [-1, 0], [-1, 1],
		[0, -1], [0, 1],
		[1, -1], [1, 0], [1, 1],
	]
	let adj: cell[] = []
	for (const pt of offset.map(([row, col]) => ({ row: c.row + row!, col: c.col + col! }))) {
		if (limit > pt.row && pt.row >= 0 && limit > pt.col && pt.col >= 0) {
			adj.push(pt)
		}
	}
	return adj
}

let part1 = 0, part2 = 0
let inputs: string[][] = []
for await (const line of console) {
	if (line == "") continue
	inputs.push(line.split(""))
}
for (let i = 0; ; i++) {
	let done = part2
	inputs.forEach((row, r) => {
		row.forEach((ch, c) => {
			if (ch == "@") {
				let cnt = 0
				for (const pt of adjacent8({ row: r, col: c }, inputs.length)) {
					if (inputs[pt.row]![pt.col] == "@") {
						cnt++
					}
				}
				if (cnt < 4) {
					if (i == 0) {
						part1++
					} else {
						inputs[r]![c] = "x"
						part2++
					}
				}

			}
		})
	})
	if (i > 0 && done == part2) {
		break
	}
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
