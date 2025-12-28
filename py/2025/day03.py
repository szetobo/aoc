import sys


def pick(line: list[int], k: int) -> int:
    start: int = 0
    res: list[int] = []
    for rem in range(k, 0, -1):
        pos, digit = start, 0
        for i in range(start, len(line) - rem + 1):
            if (v := line[i]) > digit:
                pos, digit = i, v
        res.append(digit)
        start = pos + 1
    return int("".join([str(d) for d in res]))


part1, part2 = 0, 0
while line := sys.stdin.readline().strip():
    part1 += pick([int(ch) for ch in line], 2)
    part2 += pick([int(ch) for ch in line], 12)

print(f"The result for part 1: {part1}")
print(f"The result for part 2: {part2}")
