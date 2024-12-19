package main

import (
	"fmt"
	"os"
	"slices"
	"strings"

	"github.com/spakin/awk"
)

func combo(regs []int, op int) int {
	if op >= 0 && op <= 3 {
		return op
	} else if op >= 4 && op <= 6 {
		return regs[op-4]
	}
	panic("invalid operand")
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFPat("\\d+")
		s.SetRS("")
	}

	s.AppendStmt(awk.Auto("\\d+"), func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := "", 0

		A, B, C := inputs[0][0], inputs[0][1], inputs[0][2]

		outs := eval(inputs[1], A, B, C)
		part1 = strings.Trim(strings.Replace(fmt.Sprint(outs), " ", ",", -1), "[]")

		part2 = solver(inputs[1], 0, B, C, 0)

		// fmt.Println(inputs)
		fmt.Printf("The result for part 1: %s\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func eval(prog []int, a, b, c int) (outs []int) {
	for pc := 0; pc < len(prog); {
		combo := map[int]int{0: 0, 1: 1, 2: 2, 3: 3, 4: a, 5: b, 6: c}
		op := prog[pc+1]
		switch prog[pc] {
		case 0:
			a >>= combo[op]
		case 1:
			b ^= op
		case 2:
			b = combo[op] % 8
		case 3:
			if a != 0 {
				pc = op - 2
			}
		case 4:
			b ^= c
		case 5:
			outs = append(outs, combo[op]%8)
		case 6:
			b = a >> combo[op]
		case 7:
			c = a >> combo[op]
		}
		pc += 2
	}
	return outs
}

func solver(prog []int, a, b, c, depth int) int {
	out := eval(prog, a, b, c)
	if slices.Equal(out, prog) {
		return a
	}
	if depth == 0 || slices.Equal(out, prog[len(prog)-depth:]) {
		for ni := range 8 {
			if na := solver(prog, 8*a+ni, b, c, depth+1); na > 0 {
				return na
			}
		}
	}
	return 0
}
