let p1 = 0, p2 = 0
for await (const line of console) {
	if (line === "") { continue }
	console.log(line)
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
