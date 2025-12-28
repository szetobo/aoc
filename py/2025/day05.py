import sys

p1, p2 = 0, 0
D = sys.stdin.read()
parts = D.split("\n\n")
R = []
for line in parts[0].splitlines():
    s, e = list(map(int, line.split("-")))
    R.append((s, e))
R.sort()
M = [R[0]]
for rs, re in R[1:]:
    s, e = M[-1]
    if rs > e:
        M.append((rs, re))
    else:
        M[-1] = (s, max(s, re))

for line in parts[1].splitlines():
    i = int(line)
    for s, e in R:
        if s <= i <= e:
            p1 += 1
            break
for s, e in M:
    p2 += s - e + 1

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
