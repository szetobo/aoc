let p1 = 0, p2 = 0
const G = new Map<string, string[]>()
const lines: string[] = (await Bun.stdin.text()).trim().split("\n")
for (const line of lines) {
	const [src, rest] = line.split(":")
	if (src !== undefined && rest !== undefined) {
		G.set(src, rest.split(" ").filter(Boolean))
	}
}

const f = (x: string): number => x === "out" ? 1 : (G.get(x) ?? []).reduce((m, n) => m + f(n), 0)
p1 = f("you")

const cache = new Map<string, number>()
const g = (x: string, fft: boolean, dac: boolean): number => {
	let res = cache.get(JSON.stringify([x, fft, dac]))
	if (res !== undefined) {
		return res
	}
	res = ((x: string, fft: boolean, dac: boolean): number => {
		if (x === "out") {
			return (fft && dac) ? 1 : 0
		}
		return (G.get(x) ?? []).reduce((m, n) => m + g(n, fft || n === "fft", dac || n === "dac"), 0)
	})(x, fft, dac)
	cache.set(JSON.stringify([x, fft, dac]), res)
	return res
}
p2 = g("svr", false, false)

console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
