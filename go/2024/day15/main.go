package main

import (
	"fmt"
	"os"

	"github.com/spakin/awk"
)

type cell struct {
	row, col int
}

func move(board [][]string, robot cell, movement string) cell {
	row, col := robot.row, robot.col
	switch movement {
	case "^":
		switch board[row-1][col] {
		case ".":
			return cell{row - 1, col}
		case "O":
			r := row - 2
			for ; board[r][col] == "O"; r-- {
			}
			if board[r][col] == "." {
				board[r][col] = "O"
				board[row-1][col] = "."
				return cell{row - 1, col}
			}
		}
	case ">":
		switch board[row][col+1] {
		case ".":
			return cell{row, col + 1}
		case "O":
			c := col + 2
			for ; board[row][c] == "O"; c++ {
			}
			if board[row][c] == "." {
				board[row][c] = "O"
				board[row][col+1] = "."
				return cell{row, col + 1}
			}
		}
	case "v":
		switch board[row+1][col] {
		case ".":
			return cell{row + 1, col}
		case "O":
			r := row + 2
			for ; board[r][col] == "O"; r++ {
			}
			if board[r][col] == "." {
				board[r][col] = "O"
				board[row+1][col] = "."
				return cell{row + 1, col}
			}
		}
	case "<":
		switch board[row][col-1] {
		case ".":
			return cell{row, col - 1}
		case "O":
			c := col - 2
			for ; board[row][c] == "O"; c-- {
			}
			if board[row][c] == "." {
				board[row][c] = "O"
				board[row][col-1] = "."
				return cell{row, col - 1}
			}
		}
	}
	return robot
}

func canMove(board [][]string, box cell, movement string) bool {
	row, col := box.row, box.col
	left, right := true, true
	switch movement {
	case "^":
		nr := row - 1
		line := board[nr]
		if line[col] == "#" || line[col+1] == "#" {
			return false
		} else if line[col] == "." && line[col+1] == "." {
			return true
		} else if line[col] == "[" {
			return canMove(board, cell{nr, col}, movement)
		} else {
			if line[col] == "]" {
				left = canMove(board, cell{nr, col - 1}, movement)
			}
			if line[col+1] == "[" {
				right = canMove(board, cell{nr, col + 1}, movement)
			}
			return left && right
		}
	case ">":
		if board[row][col+2] == "#" {
			return false
		} else {
			nc := col + 2
			for ; board[row][nc] == "["; nc += 2 {
			}
			return board[row][nc] == "."
		}
	case "v":
		nr := row + 1
		line := board[nr]
		if line[col] == "#" || line[col+1] == "#" {
			return false
		} else if line[col] == "." && line[col+1] == "." {
			return true
		} else if line[col] == "[" {
			return canMove(board, cell{nr, col}, movement)
		} else {
			if line[col] == "]" {
				left = canMove(board, cell{nr, col - 1}, movement)
			}
			if line[col+1] == "[" {
				right = canMove(board, cell{nr, col + 1}, movement)
			}
			return left && right
		}
	case "<":
		if board[row][col-1] == "#" {
			return false
		} else {
			nc := col - 1
			for ; board[row][nc] == "]"; nc -= 2 {
			}
			return board[row][nc] == "."
		}
	}
	return false
}

func moveBoxes(board [][]string, box cell, movement string) {
	row, col := box.row, box.col
	switch movement {
	case "^":
		nr := row - 1
		line := board[nr]
		if line[col] == "[" {
			moveBoxes(board, cell{nr, col}, movement)
		}
		if line[col] == "]" {
			moveBoxes(board, cell{nr, col - 1}, movement)
		}
		if line[col+1] == "[" {
			moveBoxes(board, cell{nr, col + 1}, movement)
		}
		line[col] = "["
		line[col+1] = "]"
		board[row][col] = "."
		board[row][col+1] = "."
	case ">":
		nc := col + 2
		for ; board[row][nc] == "["; nc += 2 {
		}
		for ; nc > col; nc -= 2 {
			board[row][nc] = "]"
			board[row][nc-1] = "["
		}
		board[row][col] = "."
	case "v":
		nr := row + 1
		line := board[nr]
		if line[col] == "[" {
			moveBoxes(board, cell{nr, col}, movement)
		}
		if line[col] == "]" {
			moveBoxes(board, cell{nr, col - 1}, movement)
		}
		if line[col+1] == "[" {
			moveBoxes(board, cell{nr, col + 1}, movement)
		}
		line[col] = "["
		line[col+1] = "]"
		board[row][col] = "."
		board[row][col+1] = "."
	case "<":
		nc := col - 1
		for ; board[row][nc] == "]"; nc -= 2 {
		}
		for ; nc < col; nc += 2 {
			board[row][nc] = "["
			board[row][nc+1] = "]"
		}
		board[row][col+1] = "."
	}
}

func move2(board [][]string, robot cell, movement string) cell {
	row, col := robot.row, robot.col
	switch movement {
	case "^":
		nr := row - 1
		switch board[nr][col] {
		case ".":
			return cell{nr, col}
		case "[":
			if canMove(board, cell{nr, col}, movement) {
				moveBoxes(board, cell{nr, col}, movement)
				return cell{nr, col}
			}
		case "]":
			if canMove(board, cell{nr, col - 1}, movement) {
				moveBoxes(board, cell{nr, col - 1}, movement)
				return cell{nr, col}
			}
		}
	case ">":
		switch board[row][col+1] {
		case ".":
			return cell{row, col + 1}
		case "[":
			if canMove(board, cell{row, col + 1}, movement) {
				moveBoxes(board, cell{row, col + 1}, movement)
				return cell{row, col + 1}
			}
		}
	case "v":
		nr := row + 1
		switch board[nr][col] {
		case ".":
			return cell{nr, col}
		case "[":
			if canMove(board, cell{nr, col}, movement) {
				moveBoxes(board, cell{nr, col}, movement)
				return cell{nr, col}
			}
		case "]":
			if canMove(board, cell{nr, col - 1}, movement) {
				moveBoxes(board, cell{nr, col - 1}, movement)
				return cell{nr, col}
			}
		}
	case "<":
		switch board[row][col-1] {
		case ".":
			return cell{row, col - 1}
		case "]":
			if canMove(board, cell{row, col - 2}, movement) {
				moveBoxes(board, cell{row, col - 2}, movement)
				return cell{row, col - 1}
			}
		}
	}
	return robot
}

func main() {
	inputs := [][]string{}
	movements := []string{}

	s := awk.NewScript()

	s.Begin = func(s *awk.Script) {
		s.SetFS("")
	}

	s.AppendStmt(awk.Auto("[#.O]+"), func(s *awk.Script) {
		inputs = append(inputs, s.FStrings())
	})

	s.AppendStmt(awk.Auto("[<>^v]+"), func(s *awk.Script) {
		movements = append(movements, s.FStrings()...)
	})

	s.End = func(s *awk.Script) {
		part1, part2 := 0, 0

		robot := cell{0, 0}
		board := make([][]string, len(inputs))
		for row, line := range inputs {
			for col, ch := range line {
				if ch == "@" {
					robot = cell{row, col}
					inputs[row][col] = "."
				}
			}
			board[row] = append(board[row], inputs[row]...)
		}

		robot1 := robot
		for _, movement := range movements {
			robot1 = move(board, robot1, movement)
		}
		for row, line := range board {
			for col, ch := range line {
				if ch == "O" {
					part1 += row*100 + col
				}
			}
		}

		board = make([][]string, len(inputs))
		for row, line := range inputs {
			board[row] = make([]string, 2*len(line))
			for col, ch := range line {
				switch ch {
				case "O":
					board[row][2*col] = "["
					board[row][2*col+1] = "]"
				case "#", ".":
					board[row][2*col] = ch
					board[row][2*col+1] = ch
				}
			}
		}

		robot2 := cell{robot.row, robot.col * 2}
		for _, movement := range movements {
			robot2 = move2(board, robot2, movement)
		}
		for row, line := range board {
			for col, ch := range line {
				if ch == "[" {
					part2 += row*100 + col
				}
			}
		}

		// for _, line := range inputs {
		// 	fmt.Println(line)
		// }
		// fmt.Println(movements)
		fmt.Printf("The result for part 1: %d\n", part1)
		fmt.Printf("The result for part 2: %d\n", part2)
	}

	if err := s.Run(os.Stdin); err != nil {
		panic(err)
	}
}
