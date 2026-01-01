import sys

p1, p2 = 0, 0
pos = 50
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]
for line in lines:
    d = -1 if line[0] == "L" else 1
    for i in range(int(line[1:])):
        pos = (pos + d) % 100
        if pos == 0:
            p2 += 1
    if pos == 0:
        p1 += 1

print(f"Part 1: {p1}")
print(f"Part 2: {p2}")
