import sys

p1, p2 = 0, 0
pos = 50
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]
for line in lines:
    d = line[0]
    num = int(line[1:])
    for i in range(num):
        pos += -1 if d == "L" else 1
        pos %= 100
        if pos == 0:
            p2 += 1
    if pos == 0:
        p1 += 1

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
