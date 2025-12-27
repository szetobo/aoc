let part1 = 0, part2 = 0
let pos = 50
for await (const line of console) {
	if (line === "") { continue }
	const tokens = line.split(/(L|R|\d+)/).filter(Boolean);
	const dir = tokens[0] === "L" ? -1 : 1
	for (let i = 0; i < Number(tokens[1]); i++) {
		pos = (pos + dir) % 100
		if (pos === 0) {
			part2++
		}
	}
	if (pos === 0) {
		part1++
	}
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
