let part1 = 0, part2 = 0
const graph = new Map<string, string[]>()
for await (const line of console) {
	if (line === "") { continue }
	const [src, rest] = line.split(":")
	if (src !== undefined && rest !== undefined) {
		graph.set(src, rest.split(" ").filter(Boolean))
	}
}

const f = (x: string): number => {
	if (x === "out") {
		return 1
	}
	let sum = 0
	for (const node of graph.get(x) ?? []) {
		sum += f(node)
	}
	return sum
}
part1 = f("you")

const cache = new Map<string, number>()
const g = (x: string, fft: string, dac: string): number => {
	let res = cache.get(x + fft + dac)
	if (res !== undefined) {
		return res
	}
	res = ((x: string, fft: string, dac: string): number => {
		if (x === "out") {
			return (fft === "1" && dac === "1") ? 1 : 0
		}
		let sum = 0
		for (const node of graph.get(x) ?? []) {
			sum += g(node, node === "fft" ? "1" : fft, node === "dac" ? "1" : dac)
		}
		return sum
	})(x, fft, dac)
	cache.set(x + fft + dac, res)
	return res
}
part2 = g("svr", "", "")
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
