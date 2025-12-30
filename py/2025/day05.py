import sys

p1, p2 = 0, 0
D = sys.stdin.read()
parts = D.split("\n\n")
R = [list(map(int, line.split("-"))) for line in parts[0].splitlines()]
R.sort()

for line in parts[1].splitlines():
    i = int(line)
    for s, e in R:
        if s <= i <= e:
            p1 += 1
            break
last = 0
for s, e in R:
    if e > last:
        p2 += e - max(s, last + 1) + 1
    last = max(last, e)

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
