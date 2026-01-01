package main

import (
	"fmt"
	"os"
	"strings"

	"github.com/spakin/awk"
)

func main() {
	inputs := []string{}

	s := awk.NewScript()

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.F(1).String())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		numPad := makePad([]string{"789", "456", "123", " 0A"})
		dirPad := makePad([]string{" ^A", "<v>"})

		for _, str := range inputs {
			ret := seq(numPad, str)
			// fmt.Println(ret, len(ret))
			ret = seq(dirPad, ret)
			// fmt.Println(ret, len(ret))
			ret = seq(dirPad, ret)
			// fmt.Println(ret, len(ret))
			part1 += len(ret) * toNum(str)

			steps := buildSteps(dirPad)
			keySeq := map[string]int{}
			ch1 := ""
			for s, i := []rune(seq(numPad, str)), 0; i < len(s)-1; i++ {
				str1, str2 := string(s[i]), string(s[i+1])
				if i == 0 {
					ch1 = str1
				}
				keySeq[str1+str2] = keySeq[str1+str2] + 1
			}
			// fmt.Println(keySeq, sum(keySeq), ch1)

			for range 25 {
				newSeq := map[string]int{}
				for k, v := range keySeq {
					next := steps[k]
					if next == "A" {
						newSeq["AA"] += v
					} else {
						for s, i := []rune(next), 0; i < len(s)-1; i++ {
							str1, str2 := string(s[i]), string(s[i+1])
							if i == 0 {
								newSeq["A"+str1] += v
							}
							newSeq[str1+str2] += v
						}
					}
				}
				next := steps["A"+ch1]
				if next == "A" {
					newSeq["AA"]++
				} else {
					for s, i := []rune(next), 0; i < len(s)-1; i++ {
						str1, str2 := string(s[i]), string(s[i+1])
						newSeq[str1+str2]++
					}
				}
				ch1 = string([]rune(next)[:1])
				keySeq = newSeq
				// fmt.Println(keySeq, sum(keySeq), ch1)
			}
			part2 += (sum(keySeq) + 1) * toNum(str)
		}

		// fmt.Println(numPad)
		// fmt.Println(dirPad)
		// fmt.Println(inputs, len(inputs))
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

type cell struct {
	row, col int
}

type pad struct {
	buttons map[string]cell
	gap     cell
}

func makePad(rows []string) pad {
	ret := pad{map[string]cell{}, cell{0, 0}}
	if len(rows) == 4 {
		ret.gap = cell{3, 0}
	}
	for row, line := range rows {
		for col, ch := range line {
			if ch != ' ' {
				ret.buttons[string(ch)] = cell{row, col}
			}
		}
	}
	return ret
}

func path(p pad, k1, k2 string) string {
	r1, c1 := p.buttons[k1].row, p.buttons[k1].col
	r2, c2 := p.buttons[k2].row, p.buttons[k2].col

	ud, lr := "", ""
	if r1 < r2 {
		ud = strings.Repeat("v", r2-r1)
	} else {
		ud = strings.Repeat("^", r1-r2)
	}
	if c1 < c2 {
		lr = strings.Repeat(">", c2-c1)
	} else {
		lr = strings.Repeat("<", c1-c2)
	}

	if c1 < c2 && (r2 != p.gap.row || c1 != p.gap.col) {
		return fmt.Sprintf("%s%sA", ud, lr)
	} else if r1 != p.gap.row || c2 != p.gap.col {
		return fmt.Sprintf("%s%sA", lr, ud)
	}
	return fmt.Sprintf("%s%sA", ud, lr)
}

func seq(p pad, s string) (ret string) {
	for str, i := []rune("A"+s), 0; i < len(str)-1; i++ {
		ret += path(p, string(str[i]), string(str[i+1]))
	}
	return
}

func toNum(s string) (ret int) {
	for _, ch := range s {
		if ch >= '0' && ch <= '9' {
			ret = ret*10 + int(ch-'0')
		}
	}
	return
}

func buildSteps(p pad) map[string]string {
	ret := map[string]string{}
	for k1 := range p.buttons {
		for k2 := range p.buttons {
			ret[k1+k2] = path(p, k1, k2)
		}
	}
	return ret
}

func sum(m map[string]int) (ret int) {
	for _, v := range m {
		ret += v
	}
	return
}
