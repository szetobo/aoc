package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"

	"github.com/spakin/awk"
)

func main() {
	inputs := [][]int{}
	curShape := 0
	shapes := [][]string{}
	shapeTiles := []int{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) { s.SetFS("") }
	s.AppendStmt(
		func(s *awk.Script) bool { return s.NF == 2 },
		func(s *awk.Script) { curShape = s.F(1).Int() },
	)
	s.AppendStmt(
		func(s *awk.Script) bool { return s.NF == 3 },
		func(s *awk.Script) {
			if len(shapes) == curShape {
				shapes = append(shapes, []string{})
				shapeTiles = append(shapeTiles, 0)
			}
			shapes[curShape] = append(shapes[curShape], s.F(0).String())
			for _, ch := range s.F(0).String() {
				if ch == '#' {
					shapeTiles[curShape]++
				}
			}
		},
	)
	s.AppendStmt(
		func(s *awk.Script) bool { return s.NF > 3 },
		func(s *awk.Script) {
			ins := []int{}
			for _, x := range regexp.MustCompile(`\d+`).FindAllString(s.F(0).String(), -1) {
				n, _ := strconv.Atoi(x)
				ins = append(ins, n)
			}
			inputs = append(inputs, ins)
		},
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		ttlTiles := 0
		for _, cnt := range shapeTiles {
			ttlTiles += cnt
		}
		for _, ins := range inputs {
			width, height := ins[0], ins[1]
			minP := (width / 3) * (height / 3)
			P, T := 0, 0
			for i, p := range ins[2 : len(ins)-1] {
				P += p
				T += shapeTiles[i] * p
			}
			if P <= minP && width*height >= T {
				part1++
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
