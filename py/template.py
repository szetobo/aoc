import sys

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]
print(lines)

print(f"Part 1: {p1}")
print(f"Part 2: {p2}")
