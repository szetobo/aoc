let part1 = 0, part2 = 0
let ranges: [number, number][] = [], inputs: number[] = []
for await (const line of console) {
	if (line === "") continue
	const [n1 = 0, n2 = 0] = line.split("-").map(Number)
	if (n2) {
		ranges.push([n1, n2])
	} else {
		inputs.push(n1)
	}
}
ranges.sort((a, b) => {
	if (a[0] !== b[0]) return a[0] - b[0]
	return a[1] - b[1]
})
let last = 0, merged = ranges.slice(0, 1)
for (const r of ranges.slice(1)) {
	const prev = merged[last]!
	if (r[0] <= prev[1]) {
		if (r[1] > prev[1]) {
			prev[1] = r[1]
		}
	} else {
		merged.push(r)
		last++
	}
}
for (const n of inputs) {
	for (const r of merged) {
		if (n >= r[0] && n <= r[1]) {
			part1++
			break
		}
	}
}
for (const r of merged) {
	part2 += r[1] - r[0] + 1
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
