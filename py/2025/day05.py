import sys

p1, p2 = 0, 0
D = sys.stdin.read()
parts = D.split("\n\n")
R = [list(map(int, line.split("-"))) for line in parts[0].splitlines()]
L = [int(line) for line in parts[1].splitlines()]

p1 = sum(next((1 for (s, e) in R if s <= n <= e), 0) for n in L)

R.sort()

lst = 0
for s, e in R:
    if e > lst:
        p2 += e - max(s, lst + 1) + 1
    lst = max(lst, e)

print(f"Part 1: {p1}")
print(f"Part 2: {p2}")
