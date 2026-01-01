package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func main() {
	inputs1 := [][]string{}
	inputs2 := [][]string{}

	s := awk.NewScript()
	s.Begin = func(s *awk.Script) {
		s.SetFS(`[: ]+`)
	}
	part := 1
	s.AppendStmt(
		func(s *awk.Script) bool { return part == 1 },
		func(s *awk.Script) { inputs1 = append(inputs1, s.FStrings()) },
	)
	s.AppendStmt(
		func(s *awk.Script) bool { return part == 2 },
		func(s *awk.Script) { inputs2 = append(inputs2, s.FStrings()) },
	)
	s.AppendStmt(
		func(s *awk.Script) bool { return s.NF == 1 },
		func(s *awk.Script) { part = 2 },
	)
	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		graph := make(map[string][]string)
		for _, x := range inputs1 {
			graph[x[0]] = x[1:]
		}
		// part1 = len(findPaths(graph, "you", "out"))

		var f func(string) int

		f = func(x string) int {
			if x == "out" {
				return 1
			}
			sum := 0
			for _, node := range graph[x] {
				sum += f(node)
			}
			return sum
		}
		part1 = f("you")

		if len(inputs2) > 0 {
			graph = make(map[string][]string)
			for _, x := range inputs2 {
				graph[x[0]] = x[1:]
			}
		}

		cache := map[string]int{}
		var g func(string, string, string) int
		g = func(x, fft, dac string) int {
			if res, ok := cache[x+fft+dac]; ok {
				return res
			}
			ret := func(x, fft, dac string) int {
				if x == "out" {
					if fft == "1" && dac == "1" {
						return 1
					}
					return 0
				}
				sum := 0
				for _, node := range graph[x] {
					fft := fft
					if node == "fft" {
						fft = "1"
					}
					dac := dac
					if node == "dac" {
						dac = "1"
					}
					sum += g(node, fft, dac)
				}
				return sum
			}(x, fft, dac)
			cache[x+fft+dac] = ret
			return ret
		}
		part2 = g("svr", "", "")

		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

// func findPaths(graph map[string][]string, start, end string) [][]string {
// 	var ret [][]string
// 	var dfs func(current, target string, path []string, visited map[string]bool)
// 	dfs = func(current, target string, path []string, visited map[string]bool) {
// 		visited[current] = true
// 		path = append(path, current)
// 		if current == target {
// 			temp := make([]string, len(path))
// 			copy(temp, path)
// 			ret = append(ret, temp)
// 		} else {
// 			for _, next := range graph[current] {
// 				if !visited[next] {
// 					dfs(next, target, path, visited)
// 				}
// 			}
// 		}
// 		delete(visited, current)
// 	}
// 	dfs(start, end, []string{}, map[string]bool{})
// 	return ret
// }
