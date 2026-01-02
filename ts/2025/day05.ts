type range = [start: number, end: number]

let p1 = 0, p2 = 0
const lines: number[] = []
const ranges: range[] = []
for await (const line of console) {
	if (line === "") { continue }
	const [n1 = 0, n2 = 0] = line.split("-").map(Number)
	if (n2) { ranges.push([n1, n2]) } else { lines.push(n1) }
}

p1 = lines.filter(n => ranges.some(([rs, re]) => rs <= n && n <= re)).length

ranges.sort((a, b) => (a[0] !== b[0]) ? a[0] - b[0] : a[1] - b[1])

let lst = 0
for (const [s, e] of ranges.slice(1)) {
	if (e > lst) { p2 += e - Math.max(s, lst + 1) + 1 }
	lst = Math.max(lst, e)
}

console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
