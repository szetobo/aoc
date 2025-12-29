import sys

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [list(line) for line in D.split("\n") if line]

M = {}
for row, s in enumerate(lines[:-1]):
    for col, ch in enumerate(s):
        if ch == "|" or ch == "S":
            cnt = M.get((row, col), 1)
            char = lines[row + 1][col]
            if char == "^":
                lines[row + 1][col - 1] = "|"
                M[(row + 1, col - 1)] = cnt + M.get((row + 1, col - 1), 0)
                lines[row + 1][col + 1] = "|"
                M[(row + 1, col + 1)] = cnt + M.get((row + 1, col + 1), 0)
                p1 += 1
            if char == "." or char == "|":
                lines[row + 1][col] = "|"
                M[(row + 1, col)] = cnt + M.get((row + 1, col), 0)

r = len(lines) - 1
p2 = sum(M.get((r, col), 0) for col, ch in enumerate(lines[-1]) if ch == "|")

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
