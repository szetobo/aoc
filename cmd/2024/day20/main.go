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
	// path     []cell
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

func edges(board [][]string, node cell) []pqItem {
	row, col := node.row, node.col
	ret := []pqItem{}
	if row > 0 && board[row-1][col] == "." {
		ret = append(ret, pqItem{node: cell{row - 1, col}, distance: 1})
	}
	if col < len(board[0]) && board[row][col+1] == "." {
		ret = append(ret, pqItem{node: cell{row, col + 1}, distance: 1})
	}
	if row < len(board) && board[row+1][col] == "." {
		ret = append(ret, pqItem{node: cell{row + 1, col}, distance: 1})
	}
	if col > 0 && board[row][col-1] == "." {
		ret = append(ret, pqItem{node: cell{row, col - 1}, distance: 1})
	}
	return ret
}

func main() {
	inputs := [][]string{}
	S, E := cell{}, cell{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
		for row, line := range inputs {
			for col, ch := range line {
				if ch == "S" {
					S = cell{row, col}
					inputs[row][col] = "."
				}
				if ch == "E" {
					E = cell{row, col}
					inputs[row][col] = "."
				}
			}
		}
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, ""

		dist := dijkstra(inputs, S, E)
		for row := 1; row <= len(inputs)-2; row++ {
			for col := 1; col <= len(inputs[0])-2; col++ {
				if inputs[row][col] == "#" {
					inputs[row][col] = "."
					newDist := dijkstra(inputs, S, E)
					if newDist != 0 && newDist <= dist-100 {
						part1++
					}
					inputs[row][col] = "#"
				}
			}
		}

		// for _, line := range inputs {
		// 	fmt.Println(line)
		// }
		fmt.Println(len(inputs), len(inputs[0]), S, E, dist)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %s\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func dijkstra(board [][]string, S, E cell) int {
	visited := map[cell]bool{}
	distances := map[cell]int{}

	pq := &queue{{S, 0}}
	heap.Init(pq)

	for pq.Len() > 0 {
		curr := heap.Pop(pq).(pqItem)
		if curr.node == E {
			return distances[E]
		}
		if visited[curr.node] {
			continue
		} else {
			visited[curr.node] = true
		}
		for _, edge := range edges(board, curr.node) {
			if visited[edge.node] {
				continue
			}
			newDist := curr.distance + edge.distance
			if _, exists := distances[edge.node]; !exists {
				distances[edge.node] = math.MaxInt
			}
			if newDist < distances[edge.node] {
				distances[edge.node] = newDist
				heap.Push(pq, pqItem{edge.node, newDist})
			}
		}
	}
	return 0
}
