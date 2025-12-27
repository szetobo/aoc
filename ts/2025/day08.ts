class DSU {
	private parents: Int32Array

	constructor(size: number) {
		this.parents = new Int32Array(size).map((_, i) => i)
	}

	find(i: number): number {
		if (this.parents[i] !== undefined) {
			if (this.parents[i] !== i) {
				this.parents[i] = this.find(this.parents[i])
			}
			return this.parents[i]
		} else {
			throw new Error("find index out of bound")
		}
	}

	union(i: number, j: number): boolean {
		const rootI = this.find(i)
		const rootJ = this.find(j)
		if (rootI !== rootJ) {
			this.parents[rootI] = rootJ
			return true
		}
		return false
	}
}

interface Point3D {
	x: number
	y: number
	z: number
}

interface Edge {
	a: number
	b: number
	w: number
}

const createEdge = (pts: Point3D[], a: number, b: number): Edge => {
	const { x: ax, y: ay, z: az } = pts[a]!
	const { x: bx, y: by, z: bz } = pts[b]!
	return {
		a, b,
		w: (ax - bx) ** 2 + (ay - by) ** 2 + (az - bz) ** 2,
	}
}

let part1 = 0, part2 = 0
const inputs: Point3D[] = []
for await (const line of console) {
	if (line === "") { continue }
	const [x = 0, y = 0, z = 0] = line.split(",").map(Number)
	inputs.push({ x, y, z })
}

const edges: Edge[] = []
for (let a = 0; a < inputs.length; a++) {
	for (let b = a + 1; b < inputs.length; b++) {
		edges.push(createEdge(inputs, a, b))
	}
}
edges.sort((a, b) => a.w - b.w)

const dsu1 = new DSU(inputs.length)
const iter = inputs.length === 20 ? 10 : inputs.length
for (let i = 0; i < iter; i++) {
	const { a, b } = edges[i]!
	dsu1.union(a, b)
}
const groups = new Map<number, number>()
for (let i = 0; i < inputs.length; i++) {
	const root = dsu1.find(i)
	groups.set(root, (groups.get(root) ?? 0) + 1)
}
part1 = [...groups.values()].sort((a, b) => b - a).slice(0, 3).reduce((m, v) => m * v, 1)

const dsu2 = new DSU(inputs.length)
let circuits = inputs.length
for (const { a, b } of edges) {
	if (dsu2.union(a, b)) {
		circuits--
		if (circuits === 1) {
			part2 = inputs[a]!.x * inputs[b]!.x
			break
		}
	}
}

console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
