package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

func getSpaceSize(blocks []int, idx int) int {
	ret := 0
	for ; idx < len(blocks) && blocks[idx] == -1; idx++ {
		ret++
	}
	return ret
}

func nextSpace(blocks []int, idx int) int {
	for ; idx < len(blocks) && blocks[idx] == -1; idx++ {
	}
	for ; idx < len(blocks) && blocks[idx] != -1; idx++ {
	}
	return idx
}

func getFileSize(blocks []int, idx int) int {
	ret := 0
	for fid := blocks[idx]; idx >= 0 && blocks[idx] == fid; idx-- {
		ret++
	}
	return ret
}

func prevFile(blocks []int, idx int) int {
	for fid := blocks[idx]; idx >= 0 && (blocks[idx] == -1 || blocks[idx] == fid); idx-- {
	}
	return idx
}

func main() {
	inputs := []int{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(nil, func(s *awk.Script) {
		inputs = s.FInts()
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		blocks := make([]int, 0, 1000)
		for i, size := range inputs {
			id := -1
			if i%2 == 0 {
				id = i / 2
			}
			for i := 0; i < size; i++ {
				blocks = append(blocks, id)
			}
		}

		disk1 := make([]int, len(blocks))
		copy(disk1, blocks)
		for i, j := 0, len(disk1)-1; i < j; {
			if disk1[i] != -1 {
				i++
				continue
			}
			if disk1[j] == -1 {
				j--
				continue
			}
			disk1[i] = disk1[j]
			disk1[j] = -1
		}
		for i := 0; disk1[i] >= 0; i++ {
			part1 += i * disk1[i]
		}

		disk2 := make([]int, len(blocks))
		copy(disk2, blocks)
		for i, j := 0, len(disk2)-1; i < j; {
			if disk2[i] != -1 {
				i++
				continue
			}
			if disk2[j] == -1 {
				j--
				continue
			}
			spaceSize := getSpaceSize(disk2, i)
			fileSize := getFileSize(disk2, j)
			if spaceSize >= fileSize {
				for s := 0; s < fileSize; s++ {
					disk2[i+s] = disk2[j-s]
					disk2[j-s] = -1
				}
				i = 0
				j = prevFile(disk2, j)
			} else {
				i = nextSpace(disk2, i)
				if i >= j {
					j = prevFile(disk2, j)
					i = 0
				}
			}
		}
		for i, val := range disk2 {
			if val == -1 {
				continue
			}
			part2 += i * val
		}

		// fmt.Println(inputs)
		// fmt.Println(blocks)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
