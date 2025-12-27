let buildLPS = (str: string): number[] => {
	const n = str.length
	const lps = new Array(n).fill(0)

	let length = 0, i = 1

	while (i < n) {
		if (str[i] === str[length]) {
			length++
			lps[i] = length
			i++
		} else {
			if (length != 0) {
				length = lps[length - 1]
			} else {
				lps[i] = 0
				i++
			}
		}
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
