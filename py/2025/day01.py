import sys

part1, part2 = 0, 0
pos = 50
while line := sys.stdin.readline().strip():
    d = line[0]
    num = int(line[1:])
    for i in range(num):
        pos += -1 if d == "L" else 1
        pos %= 100
        if pos == 0:
            part2 += 1
    if pos == 0:
        part1 += 1

print(f"The result for part 1: {part1}")
print(f"The result for part 2: {part2}")
