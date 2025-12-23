let part1 = 0, part2 = 0
let inputs: number[][] = [], ops: string[] = [], chars: string[] = []
for await (const line of console) {
	if (line === "") continue
	chars.push(line)
	if (line[0] === "+" || line[0] === "*") {
		ops = line.split(/\s+/)
	} else {
		inputs.push(line.split(/\s+/).map(Number))
	}
}
for (let i = 0; i < ops.length; i++) {
	let res = 1
	if (ops[i] === "+") {
		res = 0
	}
	for (const ins of inputs) {
		let val = ins[i] ?? 0
		if (ops[i] === "+") {
			res += val
		} else {
			res *= val
		}
	}
	part1 += res
}
let fs = true, op = "", res = 0, col = chars[0]?.length!
for (let i = 0; i < col; i++) {
	let digits: string[] = []
	for (let j = 0; j < 4; j++) {
		digits.push(chars[j]![i]!)
	}
	if (fs) {
		res = ((op = chars[4]![i]!) === "+") ? 0 : 1
		fs = !fs
	}
	let str = digits.join("")
	if (str === "    ") {
		fs = true
	} else {
		let val = Number(str)
		if (op === "+") {
			res += val
		} else {
			res *= val
		}
	}
	if (fs || i == col - 1) {
		part2 += res
	}
}
console.log("The result for part 1: %d", part1)
console.log("The result for part 2: %d", part2)
