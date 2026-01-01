package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"

	"github.com/spakin/awk"
)

func main() {
	inputs := []string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFPat("don't|do|mul\\(\\d{1,3},\\d{1,3}\\)")
		s.SetRS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = s.FStrings()
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		enabled := true
		for _, input := range inputs {
			matches := regexp.MustCompile(`don't|do|\d+`).FindAllString(input, -1)
			switch matches[0] {
			case "don't":
				enabled = false
				continue
			case "do":
				enabled = true
				continue
			default:
				n1, _ := strconv.Atoi(matches[0])
				n2, _ := strconv.Atoi(matches[1])
				part1 += n1 * n2

				if enabled {
					part2 += n1 * n2
				}
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
