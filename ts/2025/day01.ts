let p1 = 0, p2 = 0
let pos = 50
for await (const line of console) {
	if (line === "") { continue }
	const d = line[0] == "L" ? -1 : 1
	for (let i = 0; i < Number(line.slice(1)); i++) {
		pos = (pos + d) % 100
		if (pos === 0) {
			p2++
		}
	}
	if (pos === 0) {
		p1++
	}
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
