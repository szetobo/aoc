let part1 = 0, part2 = 0
for await (const line of console) {
	if (line === "") { continue }
	const parts = line.split(" ").map(x => x.slice(1, -1))
	const lights = parts.at(0)?.split("").map(x => x === "#" ? 1 : 0)
	const buttons = parts.slice(1, -1).map(x => x.split(",").map(Number))
	const joltages = parts.at(-1)?.split(",").map(Number)
	//console.log(lights, buttons, joltages)

	if (lights === undefined || joltages === undefined) { throw new Error("invalid input") }

	const f1 = (lights: number[]): number[][] => {
		const ret: number[][] = []
		for (let i = 0; i < 1 << buttons.length; i++) {
			let res = Array(lights.length).fill(0)
			let pressed = Array(buttons.length).fill(0)
			for (let btn = 0; btn < buttons.length; btn++) {
				if ((i >> btn & 1) === 1) {
					for (const b of buttons[btn]!) {
						res[b] ^= 1
					}
					pressed[btn] += 1
				}
			}
			if (res.every((v, i) => v === lights[i])) {
				ret.push(pressed)
			}
		}
		return ret
	}
	part1 += Math.min(...f1(lights).map(x => x.reduce((m, v) => m + v), 0))

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
		for (let i = 0; i < targets.length; i++) {
			if ((targets[i]! & 1) === 1) {
				lights[i] = 1
			}
		}
		for (const prePressed of f1(lights)) {
			let newJoltages = [...targets]
			prePressed.forEach((v, i) => {
				if (v === 1) {
					for (const j of buttons[i]!) {
						newJoltages[j]!--
					}
				}
			})
			if (newJoltages.every(v => v >= 0)) {
				newJoltages = newJoltages.map(v => v >>= 1)
				const p = f2(newJoltages)
				if (p != Number.MAX_SAFE_INTEGER) {
					pressed = Math.min(pressed, prePressed.reduce((m, v) => m + v, 0) + 2 * p)
				}
			}
		}
		cache.set(targets.join(","), pressed)
		return pressed
	}
	part2 += f2(joltages)
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
