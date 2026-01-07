let p1 = 0, p2 = 0
let shapes: string[][][] = []
let shapeTiles: number[] = []
for (const line of (await Bun.stdin.text()).trim().split("\n")) {
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
			const cntT = rest.reduce((m, v, i) => m + v * shapeTiles[i]!, 0)
			if (cntP <= minP && cntT <= ttlT) {
				p1++
			}
		}
	}
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
