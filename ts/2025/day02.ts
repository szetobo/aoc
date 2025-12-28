let buildLPS = (str: string): number[] => {
	const lps = new Array(str.length).fill(0)
	for (let i = 1, len = 0; i < str.length; i++) {
		while (len > 0 && str[i] !== str[len]) {
			len = lps[len - 1]
		}
		if (str[i] === str[len]) {
			len++
		}
		lps[i] = len
	}
	return lps
}

let part1 = 0, part2 = 0
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
					part1 += i
				}
				part2 += i
			}
			//if (n % 2 === 0) {
			//	n /= 2
			//	if (s.slice(0, n) === s.slice(-n)) {
			//		part1 += i
			//	}
			//}
			//if ((s + s).slice(1, -1).includes(s)) {
			//	part2 += i
			//}
		}
	})
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
