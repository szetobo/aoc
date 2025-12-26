let part1 = 0, part2 = 0
let shapes: string[][][] = []
let shapeTiles: number[] = []
for await (const line of console) {
	if (line === "") { continue }
	if (line.length === 2) {
		shapes.push([])
		shapeTiles.push(0)
	} else if (/[#.]+/.test(line)) {
		shapes.at(-1)!.push(line.split(""))
		shapeTiles[shapeTiles.length - 1]! += line.split("#").length - 1
	} else {
		const [width, height, ...rest] = line.split(/[x: ]+/).map(Number)
		if (width !== undefined && height !== undefined) {
			const ttlT = width * height
			const minP = (width / 3) * (height / 3)
			const cntP = rest.reduce((m, v) => m + v, 0)
			const cntT = rest.reduce((m, v, i) => m + v * shapeTiles[i], 0)
			if (cntP <= minP && cntT <= ttlT) {
				part1++
			}
		}
	}
}
//console.log(shapes)
//console.log(shapeTiles)
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
