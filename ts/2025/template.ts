let part1 = 0, part2 = 0
for await (const line of console) {
	if (line === "") continue
	console.log(line)
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
