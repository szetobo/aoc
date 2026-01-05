type Point = [x: number, y: number]

const minmax = (a: number, b: number): [number, number] => {
	return [Math.min(a, b), Math.max(a, b)]
}

const cache = new Map<string, boolean>()

const contains = (pts: Point[], [x, y]: Point): boolean => {
	const key = `${x},${y}`
	let ret = cache.get(key)
	if (ret !== undefined) { return ret }

	let cnt = 0
	for (let i = 0; i < pts.length; i++) {
		const [[ax, ay], [bx, by]] = [pts[i]!, pts[(i + 1) % pts.length]!]
		const [x1, x2] = minmax(ax, bx)
		const [y1, y2] = minmax(ay, by)
		if (x === x1 && x === x2 && y >= y1 && y <= y2) {
			cache.set(key, true)
			return true
		}
		if (y === y1 && y === y2 && x >= x1 && x <= x2) {
			cache.set(key, true)
			return true
		}
		if ((y1 > y) !== (y2 > y)) {
			if (x < x1 + (x2 - x1) * (y - y1) / (y2 - y1)) {
				cnt++
			}
		}
	}
	ret = cnt % 2 === 1
	cache.set(key, ret)
	return ret

}

const intersect = (pts: Point[], a: Point, b: Point): boolean => {
	const [ax, ay] = a
	const [bx, by] = b

	const key = `${ax},${ay}-${bx},${by}`
	let ret = cache.get(key)
	if (ret !== undefined) { return ret }

	for (const [i, [cx, cy]] of pts.entries()) {
		const [dx, dy] = pts[(i + 1) % pts.length]!
		const [x1, x2] = minmax(cx, dx)
		const [y1, y2] = minmax(cy, dy)
		if (x1 === x2) {
			if (ax < x1 && x1 < bx && ay < y2 && y1 < by) {
				cache.set(key, true)
				return true
			}
		} else {
			if (ay < y1 && y1 < by && ax < x2 && x1 < bx) {
				cache.set(key, true)
				return true
			}
		}
	}
	cache.set(key, false)
	return false
}

let p1 = 0, p2 = 0
const lines: Point[] = (await Bun.stdin.text()).trim().split("\n")
	.map(line => line.split(",").map(Number))
	.map(([x = 0, y = 0]) => [x, y])
for (const [i, [ax, ay]] of lines.slice(0, -2).entries()) {
	for (const [_, [bx, by]] of lines.slice(i + 1).entries()) {
		const [x1, x2] = minmax(ax, bx)
		const [y1, y2] = minmax(ay, by)

		if (x1 === x2 || y1 === y2) { continue }

		const area = (x2 - x1 + 1) * (y2 - y1 + 1)
		p1 = Math.max(p1, area)

		if (area <= p2) { continue }

		if (contains(lines, [x1, y1]) &&
			contains(lines, [x1, y2]) &&
			contains(lines, [x2, y1]) &&
			contains(lines, [x2, y2]) &&
			!intersect(lines, [x1, y1], [x2, y2])) {
			p2 = Math.max(p2, area)
		}
	}
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
