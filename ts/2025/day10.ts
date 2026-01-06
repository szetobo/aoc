let p1 = 0, p2 = 0
const lines: string[] = (await Bun.stdin.text()).trim().split("\n")
for (const line of lines) {
	const parts = line.split(" ").map(x => x.slice(1, -1))
	const lights = parts.at(0)?.split("").map(x => x === "#" ? 1 : 0)
	const buttons = parts.slice(1, -1).map(x => x.split(",").map(Number))
	const joltages = parts.at(-1)?.split(",").map(Number)

	if (lights === undefined || joltages === undefined) { throw new Error("invalid input") }

	const solutions = new Map<string, number[][]>()
	for (let i = 0; i < 1 << buttons.length; i++) {
		let res = Array(lights.length).fill(0)
		let pressed = Array(buttons.length).fill(0)
		for (const [btn, bits] of buttons.entries()) {
			if ((i >> btn & 1) === 1) {
				for (const b of bits) {
					res[b] ^= 1
				}
				pressed[btn] = 1
			}
		}
		const existing = solutions.get(res.join(""))
		if (existing) { existing.push(pressed) } else { solutions.set(res.join(""), [pressed]) }
	}

	const f1 = (lights: number[]): number[][] => solutions.get(lights.join("")) ?? []
	p1 += Math.min(...f1(lights).map(x => x.reduce((m, v) => m + v), 0))

	const cache = new Map<string, number>()
	const f2 = (targets: number[]): number => {
		const res = cache.get(targets.join(","))
		if (res !== undefined) {
			return res
		}
		if (targets.every(v => v === 0)) {
			return 0
		}
		let pressed = Number.MAX_SAFE_INTEGER
		const lights = Array(targets.length).fill(0)
		for (const [i, joltage] of targets.entries()) {
			if ((joltage & 1) === 1) {
				lights[i] = 1
			}
		}
		for (const prePressed of f1(lights)) {
			let newTarget = [...targets]
			for (const [i, v] of prePressed.entries()) {
				if (v === 1) {
					for (const b of buttons[i]!) {
						newTarget[b]!--
					}
				}
			}

			if (newTarget.some(v => v < 0)) { continue }

			newTarget = newTarget.map(v => v >>= 1)
			const p = f2(newTarget)
			if (p != Number.MAX_SAFE_INTEGER) {
				pressed = Math.min(pressed, prePressed.reduce((m, v) => m + v, 0) + 2 * p)
			}
		}
		cache.set(targets.join(","), pressed)
		return pressed
	}
	p2 += f2(joltages)
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
