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
	path     []cell
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
		part1, part2 := 0, 0

		dest := dijkstra(inputs, S, E)
		part1 = cheat(dest.path, 100, 2)
		part2 = cheat(dest.path, 100, 20)

		// fmt.Println(len(inputs), len(inputs[0]), S, E)
		// fmt.Println(dest.node, dest.distance)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}

func dijkstra(board [][]string, S, E cell) pqItem {
	visited := map[cell]bool{}
	distances := map[cell]int{}

	pq := &queue{{S, 0, []cell{S}}}
	heap.Init(pq)

	for pq.Len() > 0 {
		curr := heap.Pop(pq).(pqItem)
		if curr.node == E {
			// return distances[E]
			return curr
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
				heap.Push(pq, pqItem{edge.node, newDist, append(curr.path, edge.node)})
			}
		}
	}
	return pqItem{}
}

func cheat(path []cell, save, cheat int) int {
	ret := 0
	for i, s := range path[:len(path)-save] {
		for j, e := range path[i+save:] {
			d := int(math.Abs(float64(e.row-s.row)) + math.Abs(float64(e.col-s.col)))
			if d <= cheat && d <= j {
				ret++
			}
		}
	}
	return ret
}
