import sys

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]
print(lines)

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
