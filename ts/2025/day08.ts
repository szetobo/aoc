class Point3D {
	constructor(public x: number, public y: number, public z: number) {}

	dist(other: Point3D): number {
		return (this.x - other.x) ** 2 + (this.y - other.y) ** 2 + (this.z - other.z) ** 2
	}
}

const createDSU = (size: number) => {
	const parent = new Int32Array(size).map((_, i) => i)

	const find = (i: number): number => {
		if (parent[i] !== undefined) {
			if (parent[i] !== i) {
				parent[i] = find(parent[i])
			}
			return parent[i]
		} else {
			throw new Error("find index out of bound")
		}
	}

	const union = (i: number, j: number): boolean => {
		const rootI = find(i)
		const rootJ = find(j)
		if (rootI !== rootJ) {
			parent[rootI] = rootJ
			return true
		}
		return false
	}

	return { find, union }
}


let part1 = 0, part2 = 0
const inputs: Point3D[] = []
for await (const line of console) {
	if (line === "") { continue }
	const [n1 = 0, n2 = 0, n3 = 0] = line.split(",").map(Number)
	inputs.push(new Point3D(n1, n2, n3))
}

const edges: { a: number; b: number; w: number }[] = []
for (let a = 0; a < inputs.length; a++) {
	for (let b = a + 1; b < inputs.length; b++) {
		const w = inputs[a]!.dist(inputs[b]!)
		edges.push({ a, b, w })
	}
}
edges.sort((a, b) => a.w - b.w)

const dsu1 = createDSU(inputs.length)
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

const dsu2 = createDSU(inputs.length)
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
