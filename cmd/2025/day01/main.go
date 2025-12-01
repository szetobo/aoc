package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

type X struct {
	dir string
	num int
}

// func divmod(x, y int) (q, r int) {
// 	q = x / y
// 	r = x % y
// 	return
// }

func main() {
	inputs := []X{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFPat(`L|R|\d+`)
	}
	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, X{s.F(1).String(), s.F(2).Int()})
	})
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0
		i := 50
		for _, x := range inputs {
			d := 1
			if x.dir == "L" {
				d = -1
			}
			for range x.num {
				i = (i + d) % 100
				if i == 0 {
					part2++
				}
			}
			if i == 0 {
				part1++
			}
		}
		// for _, x := range inputs {
		// 	q, r := divmod(x.num, 100)
		// 	part2 += q
		// 	if x.dir == "L" {
		// 		if i != 0 && r > i {
		// 			part2++
		// 		}
		// 		i = (i - r + 100) % 100
		// 	} else {
		// 		if r+i > 100 {
		// 			part2++
		// 		}
		// 		i = (i + r) % 100
		// 	}
		// 	if i == 0 {
		// 		part1++
		// 		part2++
		// 	}
		// }

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
