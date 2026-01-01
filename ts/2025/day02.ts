let buildLPS = (s: string): number[] => {
	const lps = new Array(s.length).fill(0)
	for (let i = 1, j = 0; i < s.length; i++) {
		while (j > 0 && s[i] !== s[j]) {
			j = lps[j - 1]
		}
		if (s[i] === s[j]) {
			j++
		}
		lps[i] = j
	}
	return lps
}

let p1 = 0, p2 = 0
for await (const line of console) {
	if (line === "") { continue }
	line.split(",").map(pair => {
		const [n1, n2] = pair.split("-")
		for (let i = Number(n1); i <= Number(n2); i++) {
			let s = i.toString(), n = s.length
			let lps = buildLPS(s)
			let longest = Number(lps[n - 1]), period = n - longest
			if (longest > 0 && n % period === 0) {
				if ((longest / period) % 2 === 1) {
					p1 += i
				}
				p2 += i
			}
			// if (n % 2 === 0) {
			// 	n /= 2
			// 	if (s.slice(0, n) === s.slice(-n)) {
			// 		p1 += i
			// 	}
			// }
			// if ((s + s).slice(1, -1).includes(s)) {
			// 	p2 += i
			// }
		}
	})
}
console.log("Part 1: %d", p1)
console.log("Part 2: %d", p2)
