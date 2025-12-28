import sys
import itertools
from typing import NamedTuple


class Pt(NamedTuple):
    row: int
    col: int


def adjacent8(c: Pt, limit: int) -> list[Pt]:
    offset: list[Pt] = [
        Pt(-1, -1),
        Pt(-1, 0),
        Pt(-1, 1),
        Pt(0, -1),
        Pt(0, 1),
        Pt(1, -1),
        Pt(1, 0),
        Pt(1, 1),
    ]
    adj: list[Pt] = []
    for pt in [Pt(c.row + pt.row, c.col + pt.col) for pt in offset]:
        if all(v >= 0 and v < limit for v in pt):
            adj.append(pt)
    return adj


part1, part2 = 0, 0
inputs: list[list[str]] = []
while line := sys.stdin.readline().strip():
    inputs.append([ch for ch in line])

for i in itertools.count(0):
    done = part2
    for row, line in enumerate(inputs):
        for col, ch in enumerate(line):
            if ch == "@":
                cnt = len(
                    [
                        pt
                        for pt in adjacent8(Pt(row, col), len(inputs))
                        if inputs[pt.row][pt.col] == "@"
                    ]
                )
                if cnt < 4:
                    if i == 0:
                        part1 += 1
                    else:
                        line[col] = "x"
                        part2 += 1
    if i > 0 and done == part2:
        break

print(f"The result for part 1: {part1}")
print(f"The result for part 2: {part2}")
