package main

import (
	"container/heap"
	"fmt"
	"math"
	"os"

	"github.com/spakin/awk"
)

type cell struct {
	row, col int
}

type pqItem struct {
	node     cell
	distance int
}

type queue []pqItem

func (pq queue) Len() int            { return len(pq) }
func (pq queue) Less(i, j int) bool  { return pq[i].distance < pq[j].distance }
func (pq queue) Swap(i, j int)       { pq[i], pq[j] = pq[j], pq[i] }
func (pq *queue) Push(x interface{}) { *pq = append(*pq, x.(pqItem)) }
func (pq *queue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	*pq = old[:n-1]
	return item
}

func buildEdge(board [][]int, node cell) []pqItem {
	row, col := node.row, node.col
	ret := []pqItem{}
	for _, edge := range []cell{{row - 1, col}, {row, col + 1}, {row + 1, col}, {row, col - 1}} {
		if edge.row >= 0 && edge.row < len(board) && edge.col >= 0 && edge.col < len(board[0]) {
			if board[edge.row][edge.col]-board[row][col] == 1 {
				ret = append(ret, pqItem{edge, 1})
			}
		}
	}
	return ret
}

func findPaths(predecessors map[cell][]cell, current, source cell, path []cell, allPaths *[][]cell) {
	if current == source {
		*allPaths = append(*allPaths, path)
		return
	}
	for _, pred := range predecessors[current] {
		findPaths(predecessors, pred, source, append(path, pred), allPaths)
	}
}

func main() {
	inputs := [][]int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FInts())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		initPos := []cell{}
		graph := map[cell]map[cell]int{}

		for row, line := range inputs {
			for col, val := range line {
				node := cell{row, col}
				if val == 0 {
					initPos = append(initPos, node)
				}
				for _, edge := range buildEdge(inputs, node) {
					if _, exists := graph[node]; !exists {
						graph[node] = map[cell]int{}

					}
					graph[node][edge.node] = edge.distance
				}
			}
		}
		for _, start := range initPos {
			pq := &queue{{start, 0}}
			heap.Init(pq)

			distances := map[cell]int{}
			predecessors := map[cell][]cell{}
			visited := map[cell]bool{}

			for pq.Len() > 0 {
				curr := heap.Pop(pq).(pqItem)
				if visited[curr.node] {
					continue
				}
				visited[curr.node] = true

				for edge, dist := range graph[curr.node] {
					if visited[edge] {
						continue
					}
					newDist := curr.distance + dist
					curDist := math.MaxInt
					if _, exists := distances[edge]; !exists {
						distances[edge] = curDist
					}
					if newDist < distances[edge] {
						distances[edge] = newDist
						predecessors[edge] = []cell{curr.node}
						heap.Push(pq, pqItem{edge, newDist})
					} else if newDist == distances[edge] {
						predecessors[edge] = append(predecessors[edge], curr.node)
					}
				}
			}
			for node, v := range distances {
				if v == 9 {
					part1++

					allPaths := [][]cell{}
					findPaths(predecessors, node, start, []cell{node}, &allPaths)
					part2 += len(allPaths)
				}
			}
		}

		// fmt.Println(inputs)
		// fmt.Println(initPos)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
