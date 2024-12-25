package main

import (
	"fmt"
	"maps"
	"os"
	"slices"
	"strings"

	"github.com/spakin/awk"
)

func main() {
	inputs := [][]string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("[-]")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, ""

		conns := map[string]map[string]bool{}
		res := map[string]bool{}
		for _, line := range inputs {
			if _, exists := conns[line[0]]; !exists {
				conns[line[0]] = make(map[string]bool)
			}
			if _, exists := conns[line[1]]; !exists {
				conns[line[1]] = make(map[string]bool)
			}
			conns[line[0]][line[1]] = true
			conns[line[1]][line[0]] = true
		}
		for key, conn := range conns {
			if strings.HasPrefix(key, "t") {
				keys := slices.Collect(maps.Keys(conn))
				for i, k := range keys {
					for j := i + 1; j < len(keys); j++ {
						if conns[k][keys[j]] {
							str := []string{key, k, keys[j]}
							slices.Sort(str)
							res[strings.Join(str, ",")] = true
						}
					}
				}
			}
		}
		part1 = len(res)

		cliques := [][]string{}
		r := map[string]bool{}
		p := map[string]bool{}
		x := map[string]bool{}
		for node := range conns {
			p[node] = true
		}
		BK(r, p, x, conns, &cliques)
		res2 := []string{}
		for _, clique := range cliques {
			if len(clique) > len(res2) {
				res2 = clique
			}
		}
		slices.Sort(res2)
		part2 = strings.Join(res2, ",")

		// fmt.Println(inputs)
		// fmt.Println(len(inputs), len(conns))
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %s\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func BK(r, p, x map[string]bool, graph map[string]map[string]bool, result *[][]string) {
	if len(p) == 0 && len(x) == 0 {
		clique := slices.Collect(maps.Keys(r))
		*result = append(*result, clique)
		return
	}

	pivot := ""
	for node := range p {
		pivot = node
		break
	}
	for node := range p {
		if graph[pivot][node] {
			continue
		}
		newR := map[string]bool{}
		maps.Copy(newR, r)
		newR[node] = true

		newP := map[string]bool{}
		newX := map[string]bool{}
		for edge := range graph[node] {
			if p[edge] {
				newP[edge] = true
			}
			if x[edge] {
				newX[edge] = true
			}
		}

		BK(newR, newP, newX, graph, result)

		delete(p, node)
		x[node] = true
	}
}
