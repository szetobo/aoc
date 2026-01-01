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

type state struct {
	node      cell
	direction string
}

type pqItem struct {
	node      cell
	distance  int
	direction string
	path      []cell
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

func edges(board [][]string, node cell, direction string) []pqItem {
	row, col := node.row, node.col
	ret := []pqItem{}
	if direction != "v" && board[row-1][col] == "." {
		distance := 1
		if direction != "^" {
			distance += 1000
		}
		ret = append(ret, pqItem{node: cell{row - 1, col}, distance: distance, direction: "^"})
	}
	if direction != "<" && board[row][col+1] == "." {
		distance := 1
		if direction != ">" {
			distance += 1000
		}
		ret = append(ret, pqItem{node: cell{row, col + 1}, distance: distance, direction: ">"})
	}
	if direction != "^" && board[row+1][col] == "." {
		distance := 1
		if direction != "v" {
			distance += 1000
		}
		ret = append(ret, pqItem{node: cell{row + 1, col}, distance: distance, direction: "v"})
	}
	if direction != ">" && board[row][col-1] == "." {
		distance := 1
		if direction != "<" {
			distance += 1000
		}
		ret = append(ret, pqItem{node: cell{row, col - 1}, distance: distance, direction: "<"})
	}
	return ret
}

func main() {
	inputs := [][]string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		posS, posE := cell{}, cell{}

		board := make([][]string, len(inputs))
		for row, line := range inputs {
			for col, ch := range line {
				if ch == "S" {
					posS = cell{row, col}
					inputs[row][col] = "."
				} else if ch == "E" {
					posE = cell{row, col}
					inputs[row][col] = "."
				}
			}
			board[row] = append(board[row], inputs[row]...)
		}

		visited := map[state]bool{}
		distances := map[cell]int{}
		results := []pqItem{}

		pq := &queue{{node: posS, distance: 0, direction: ">", path: []cell{posS}}}
		heap.Init(pq)

		for pq.Len() > 0 {
			curr := heap.Pop(pq).(pqItem)
			if curr.node == posE {
				results = append(results, curr)
			}

			for _, edge := range edges(board, curr.node, curr.direction) {
				if visited[state{edge.node, edge.direction}] {
					continue
				}
				newDist := curr.distance + edge.distance
				curDist := math.MaxInt
				if _, exists := distances[edge.node]; !exists {
					distances[edge.node] = curDist
				}
				if newDist < distances[edge.node] {
					distances[edge.node] = newDist
				}
				path := append([]cell{}, curr.path...)
				path = append(path, edge.node)
				heap.Push(pq, pqItem{edge.node, newDist, edge.direction, path})
			}
			visited[state{curr.node, curr.direction}] = true
		}
		part1 = distances[posE]

		tiles := map[cell]bool{}
		for _, res := range results {
			if res.distance == distances[posE] {
				for _, node := range res.path {
					tiles[node] = true
				}
			}
		}
		part2 = len(tiles)

		// fmt.Println(inputs)
		// fmt.Println(posS, posE)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
