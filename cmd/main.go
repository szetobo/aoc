package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func main() {
	inputs := []string{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFS("")
		// s.SetFPat(`\d+`)
	}
	s.AppendStmt(
		func(s *awk.Script) bool { return s.NF > 0 },
		func(s *awk.Script) { inputs = append(inputs, s.F(0).String()) },
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for _, x := range inputs {
			fmt.Println(x)
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
