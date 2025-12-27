type range = [start: number, end: number]

let part1 = 0, part2 = 0
const inputs: number[] = []
const ranges: range[] = []
for await (const line of console) {
	if (line === "") { continue }
	const [n1 = 0, n2 = 0] = line.split("-").map(Number)
	if (n2) { ranges.push([n1, n2]) } else { inputs.push(n1) }
}
ranges.sort((a, b) => (a[0] !== b[0]) ? a[0] - b[0] : a[1] - b[1])
const merged = ranges.slice(0, 1)
for (const r of ranges.slice(1)) {
	const prev = merged.at(-1)!
	if (r[0] > prev[1]) {
		merged.push(r)
	} else {
		prev[1] = Math.max(r[1], prev[1])

	}
}
part1 = inputs.filter(n => merged.some(([rs, re]) => n >= rs && n <= re)).length
part2 = merged.reduce((m, [rs, re]) => m + (re - rs + 1), 0)

console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
