let pickK = (s: string, n: number): number => {
	let res: string[] = Array(n).fill("0")
	for (let i = n, sta = 0; i > 0; i--) {
		let p = sta, d = "0"
		for (let j = sta; j < s.length - i + 1; j++) {
			const val = s[j]!
			if (val > d) {
				p = j, d = val
			}
		}
		res[n - i] = d
		sta = p + 1
	}
	return Number(res.join(""))

}

let p1 = 0, p2 = 0
for await (const line of console) {
	if (line === "") { continue }
	p1 += pickK(line, 2)
	p2 += pickK(line, 12)
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
