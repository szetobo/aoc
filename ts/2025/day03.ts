let pickK = (line: number[], k: number): number => {
	const n = line.length
	let res: number[] = []
	let start = 0
	for (let remaining = k; remaining > 0; remaining--) {
		let bestPos = start, bestDigit = 0
		for (let i = start; i < n - (remaining - 1); i++) {
			if (line[i]! > bestDigit) {
				bestDigit = line[i]!
				bestPos = i
			}
		}
		res = [...res, bestDigit]
		start = bestPos + 1
	}
	return Number(res.join(""))

}

let part1 = 0, part2 = 0
for await (const line of console) {
	if (line == "") continue
	const tokens = line.split(/(\d)/).filter(Boolean).map(Number);
	part1 += pickK(tokens, 2)
	part2 += pickK(tokens, 12)
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
