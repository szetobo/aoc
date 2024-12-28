package main

import (
	"fmt"
	"os"
	"slices"
	"strings"

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
		part1, part2 := 0, ""

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
		part1 = calc(wires, "z")

		cinWire := ""
		bit := 0
		swapped := []string{}
		for bit < 45 {
			xWire := fmt.Sprintf("x%02d", bit)
			yWire := fmt.Sprintf("y%02d", bit)
			zWire := fmt.Sprintf("z%02d", bit)
			if bit == 0 {
				cinWire = findConn(conns, xWire, yWire, "AND")
			} else {
				xXORy := findConn(conns, xWire, yWire, "XOR")
				xXORyXORcin := findConn(conns, xXORy, cinWire, "XOR")
				if xXORyXORcin != zWire {
					// fmt.Printf("%s, %s, %s != %s\n", xXORy, cinWire, xXORyXORcin, zWire)
					if xXORyXORcin == "" {
						conn := conns[zWire]
						if conn[0] == cinWire {
							xXORyXORcin = conn[2]
						} else {
							xXORyXORcin = conn[0]
						}
						swapped = append(swapped, xXORy, xXORyXORcin)
						conns[xXORy], conns[xXORyXORcin] = conns[xXORyXORcin], conns[xXORy]
						xXORy = xXORyXORcin
					} else {
						swapped = append(swapped, zWire, xXORyXORcin)
						conns[xXORyXORcin], conns[zWire] = conns[zWire], conns[xXORyXORcin]
					}
				}
				xANDy := findConn(conns, xWire, yWire, "AND")
				xXORyANDcin := findConn(conns, xXORy, cinWire, "AND")
				cinWire = findConn(conns, xANDy, xXORyANDcin, "OR")
			}
			bit++

		}
		slices.Sort(swapped)
		part2 = strings.Join(swapped, ",")

		// fmt.Println(wires, len(wires))
		// fmt.Println(conns, len(conns))
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %s\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func calc(wires map[string]int, wire string) int {
	val := 0
	for i := 0; ; i++ {
		str := fmt.Sprintf("%s%02d", wire, i)
		if _, exists := wires[str]; !exists {
			break
		}
		val += wires[str] << i
	}
	return val
}

func findConn(conns map[string][]string, a, b, gate string) string {
	for k, v := range conns {
		if slices.Compare(v, []string{a, gate, b}) == 0 ||
			slices.Compare(v, []string{b, gate, a}) == 0 {
			return k
		}
	}
	return ""
}
