type Point3D = [x: number, y: number, z: number]
type Edge = [a: Point3D, b: Point3D, w: number]

class DSU<T> {
	private parents: Map<T, T>

	constructor() {
		this.parents = new Map<T, T>()
	}

	find(pt: T): T {
		let res = this.parents.get(pt)
		if (res === undefined) {
			this.parents.set(pt, pt)
			return pt
		} else if (res !== pt) {
			res = this.find(res)
			this.parents.set(pt, res)
		}
		return res
	}

	union(a: T, b: T): boolean {
		const rootI = this.find(a)
		const rootJ = this.find(b)
		if (rootI !== rootJ) {
			this.parents.set(rootI, rootJ)
			return true
		}
		return false
	}
}

let p1 = 0, p2 = 0
const lines: Point3D[] = (await Bun.stdin.text()).trim().split("\n")
	.map(line => line.split(",").map(Number))
	.map(([x = 0, y = 0, z = 0]) => [x, y, z])

const edges: Edge[] = []
for (const [i, a] of lines.entries()) {
	for (const [_, b] of lines.slice(i + 1).entries()) {
		const [ax, ay, az] = a
		const [bx, by, bz] = b
		const w = (ax - bx) ** 2 + (ay - by) ** 2 + (az - bz) ** 2
		edges.push([a, b, w])

	}
}
edges.sort((a, b) => a[2] - b[2])

const n = lines.length
const dsu1 = new DSU<Point3D>()
const iter = n === 20 ? 10 : n
for (let i = 0; i < iter; i++) {
	const [a, b] = edges[i]!
	dsu1.union(a, b)
}
const groups = new Map<Point3D, number>()
for (const pt of lines) {
	const root = dsu1.find(pt)
	groups.set(root, (groups.get(root) ?? 0) + 1)
}
p1 = [...groups.values()].sort((a, b) => b - a).slice(0, 3).reduce((m, v) => m * v, 1)

const dsu2 = new DSU<Point3D>()
let circuits = n
for (const [a, b] of edges) {
	if (dsu2.union(a, b)) {
		circuits--
		if (circuits === 1) {
			p2 = a[0] * b[0]
			break
		}
	}
}

console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
