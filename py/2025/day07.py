import sys
from itertools import product
from collections import defaultdict

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [list(line) for line in D.split("\n") if line]

M = defaultdict(int)
n = len(lines)
for row, col in product(range(n - 1), range(n - 1)):
    ch = lines[row][col]
    if ch == "|" or ch == "S":
        cnt = M.get((row, col), 1)
        char = lines[row + 1][col]
        if char == "^":
            lines[row + 1][col - 1] = "|"
            M[(row + 1, col - 1)] += cnt
            lines[row + 1][col + 1] = "|"
            M[(row + 1, col + 1)] += cnt
            p1 += 1
        if char == "." or char == "|":
            lines[row + 1][col] = "|"
            M[(row + 1, col)] += cnt

r = len(lines) - 1
p2 = sum(M.get((r, col), 0) for col, ch in enumerate(lines[-1]) if ch == "|")

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
