type point = [row: number, col: number]

const adjacent8 = ([row, col]: point, limit: number): point[] => {
	const offsets: point[] = [
		[-1, -1], [-1, 0], [-1, 1],
		[0, -1], [0, 1],
		[1, -1], [1, 0], [1, 1],
	]
	return offsets.map(([dr, dc]) => [row + dr, col + dc] as point)
		.filter(pt => pt.every(v => v >= 0 && v < limit))
}

let p1 = 0, p2 = 0
let lines: string[][] = []
for await (const line of console) {
	if (line === "") { continue }
	lines.push(line.split(""))
}
for (let i = 0; ; i++) {
	let done = p2
	for (const [row, line] of lines.entries()) {
		for (const [col, ch] of line.entries()) {
			if (ch === "@") {
				const cnt = adjacent8([row, col], lines.length).filter(([r, c]) => lines[r]?.[c] === "@").length
				if (cnt < 4) {
					if (i === 0) {
						p1++
					} else {
						line[col] = "x"
						p2++
					}
				}
			}
		}
	}
	if (i > 0 && (done - p2) === 0) {
		break
	}
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
