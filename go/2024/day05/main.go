package main

import (
	"fmt"
	"os"
	"slices"

	"github.com/spakin/awk"
)

func correctOrder(pageOrders map[int]map[int]bool, pages []int) bool {
	if len(pages) < 2 {
		return true
	}

	ret := true
	for i := range pages[1:] {
		if pageOrders[pages[i+1]][pages[i]] {
			ret = false
			break
		}
	}
	return ret
}

func main() {
	// pageOrders := [][]int{}
	pageOrders := map[int]map[int]bool{}
	updates := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("[,|]")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		if s.NF == 2 {
			a, b := s.FInts()[0], s.FInts()[1]
			if _, exists := pageOrders[a]; !exists {
				pageOrders[a] = make(map[int]bool)
			}
			pageOrders[a][b] = true
		} else if s.NF > 1 {
			updates = append(updates, s.FInts())
		}
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		for _, pages := range updates {
			inOrder := true
			for i := range pages {
				if !correctOrder(pageOrders, pages[i:]) {
					inOrder = false
					break
				}
			}
			if inOrder {
				part1 += pages[len(pages)/2]
			} else {
				slices.SortFunc(pages, func(i, j int) int {
					if pageOrders[i][j] {
						return 1
					} else {
						return -1
					}
				})
				part2 += pages[len(pages)/2]
			}
		}

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
