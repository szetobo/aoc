import sys
from itertools import count, product


def adjacent8(c: tuple[int, int], limit: int) -> list[tuple[int, int]]:
    row, col = c
    offset: tuple[tuple[int, int], ...] = (
        (-1, -1),
        (-1, 0),
        (-1, 1),
        (0, -1),
        (0, 1),
        (1, -1),
        (1, 0),
        (1, 1),
    )
    return [
        (row, col)
        for (row, col) in [(row + dr, col + dc) for (dr, dc) in offset]
        if 0 <= row < limit and 0 <= col < limit
    ]


p1, p2 = 0, 0
D = sys.stdin.read()
lines: list[list[str]] = [list(line) for line in D.split("\n") if line]
n = len(lines)

for i in count(0):
    done = p2
    for row, col in product(range(n), range(n)):
        if lines[row][col] == "@":
            if sum(1 for (r, c) in adjacent8((row, col), n) if lines[r][c] == "@") < 4:
                if i == 0:
                    p1 += 1
                else:
                    lines[row][col] = "x"
                    p2 += 1
    if i > 0 and done == p2:
        break

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
