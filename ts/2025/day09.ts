type Point = { x: number, y: number }

const minmax = (a: number, b: number): [number, number] => {
	return [Math.min(a, b), Math.max(a, b)]
}

const cache = new Map<string, boolean>()

const contains = (pts: Point[], { x, y }: Point): boolean => {
	const key = `${x},${y}`
	let ret = cache.get(key)
	if (ret !== undefined) { return ret }

	let cnt = 0
	for (let i = 0; i < pts.length; i++) {
		const [a, b] = [pts[i]!, pts[(i + 1) % pts.length]!]
		const [x1, x2] = minmax(a.x, b.x)
		const [y1, y2] = minmax(a.y, b.y)
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
	const { x: ax, y: ay } = a
	const { x: bx, y: by } = b

	const key = `${ax},${ay}-${bx},${by}`
	let ret = cache.get(key)
	if (ret !== undefined) { return ret }

	for (let i = 0; i < pts.length; i++) {
		const [a, b] = [pts[i]!, pts[(i + 1) % pts.length]!]
		const [x1, x2] = minmax(a.x, b.x)
		const [y1, y2] = minmax(a.y, b.y)
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

let part1 = 0, part2 = 0
const inputs: Point[] = []
for await (const line of console) {
	if (line === "") { continue }
	const [x, y] = line.split(",").map(Number)
	if (x !== undefined && y !== undefined) {
		inputs.push({ x, y })
	}
}
for (const [i, a] of inputs.slice(0, -2).entries()) {
	for (const [_, b] of inputs.slice(i + 1).entries()) {
		const [x1, x2] = minmax(a.x, b.x)
		const [y1, y2] = minmax(a.y, b.y)

		if (x1 === x2 || y1 === y2) { continue }

		const area = (x2 - x1 + 1) * (y2 - y1 + 1)
		part1 = Math.max(part1, area)

		if (area <= part2) { continue }

		if (contains(inputs, { x: x1, y: y1 }) &&
			contains(inputs, { x: x1, y: y2 }) &&
			contains(inputs, { x: x2, y: y1 }) &&
			contains(inputs, { x: x2, y: y2 }) &&
			!intersect(inputs, { x: x1, y: y1 }, { x: x2, y: y2 })) {
			part2 = Math.max(part2, area)
		}
	}
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
