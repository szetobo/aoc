package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func main() {
	wires := map[string]int{}
	conns := map[string][]string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("[ :>-]+")
	}

	s.AppendStmt(awk.Auto(": \\d"), func(s *awk.Script) {
		wires[s.F(1).String()] = s.F(2).Int()
	})

	s.AppendStmt(awk.Auto("->"), func(s *awk.Script) {
		conns[s.F(4).String()] = s.FStrings()[:3]
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for count := len(conns); count > 0; {
			for k, v := range conns {
				if _, exists := wires[k]; exists {
					continue
				}
				w1, w2 := v[0], v[2]
				if _, exists := wires[w1]; exists {
					if _, exists := wires[w2]; exists {
						a, b := wires[w1], wires[w2]
						switch v[1] {
						case "AND":
							wires[k] = a & b
						case "OR":
							wires[k] = a | b
						case "XOR":
							wires[k] = a ^ b
						}
						count--
					}
				}
			}
		}
		for i := 0; ; i++ {
			str := fmt.Sprintf("z%02d", i)
			if _, exists := wires[str]; !exists {
				break
			}
			part1 += wires[str] << i
		}

		// fmt.Println(wires, len(wires))
		// fmt.Println(conns, len(conns))

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
