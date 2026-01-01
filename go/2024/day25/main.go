package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func main() {
	locks := [][]int{}
	keys := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetRS("")
	}

	s.AppendStmt(awk.Auto("[.#]+"), func(s *awk.Script) {
		lines := s.FStrings()
		if lines[0] == "....." {
			res := make([]int, 5)
			for i, line := range lines {
				for j, ch := range line {
					if ch == '.' {
						res[j] = 5 - i
					}
				}
			}
			keys = append(keys, res)
		} else {
			res := make([]int, 5)
			for i, line := range lines {
				for j, ch := range line {
					if ch == '#' {
						res[j] = i
					}
				}
			}
			locks = append(locks, res)
		}
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for _, lock := range locks {
			for _, key := range keys {
				res := true
				for i := range 5 {
					if lock[i]+key[i] > 5 {
						res = false
						break
					}
				}
				if res {
					part1++
				}
			}
		}

		// fmt.Println(locks)
		// fmt.Println(keys)
		// fmt.Println(len(keys), len(locks))

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
