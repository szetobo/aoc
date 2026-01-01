package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func btoi(ds []byte) int {
	res := 0
	for _, d := range ds {
		if d == ' ' {
			continue
		}
		res = res*10 + int(d-'0')
	}
	return res
}

func main() {
	inputs := [][]int{}
	ops := []string{}
	chars := [][]byte{}

	s := awk.NewScript()
	s.AppendStmt(nil, func(s *awk.Script) {
		if s.NR == 5 {
			ops = s.FStrings()
		} else {
			inputs = append(inputs, s.FInts())
		}
		chars = append(chars, []byte(s.F(0).String()))
	})
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for i, op := range ops {
			res := 1
			if op == "+" {
				res = 0
			}
			for j := range len(inputs) {
				if op == "+" {
					res += inputs[j][i]
				} else {
					res *= inputs[j][i]
				}
			}
			part1 += res
		}
		fs := true
		var op byte
		res := 0
		col := len(chars[0])
		for i := range col {
			digits := []byte{}
			for j := range 4 {
				digits = append(digits, chars[j][i])
			}
			if fs {
				op = chars[4][i]
				if op == '+' {
					res = 0
				} else {
					res = 1
				}
				fs = false
			}
			if string(digits) == "    " {
				fs = true
			} else {
				if op == '+' {
					res += btoi(digits)
				} else {
					res *= btoi(digits)
				}
			}
			if fs || i == col-1 {
				part2 += res
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
