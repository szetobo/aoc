import sys
from functools import cache
from itertools import combinations, chain


p1, p2 = 0, 0
D = sys.stdin.read()
lines = [list(map(int, line.split(","))) for line in D.split("\n") if line]


@cache
def valid(x, y) -> bool:
    cnt = 0
    for i, (x1, y1) in enumerate(lines):
        x2, y2 = lines[(i + 1) % len(lines)]
        x1, x2 = [x1, x2] if x1 <= x2 else [x2, x1]
        y1, y2 = [y1, y2] if y1 <= y2 else [y2, y1]
        if x == x1 and x == x2 and y1 <= y <= y2:
            return True
        if y == y1 and y == y2 and x1 <= x <= x2:
            return True
        if (y1 > y) != (y2 > y):
            if x < x1 + (x2 - x1) * (y - y1) / (y2 - y1):
                cnt += 1
    return (cnt % 2) == 1


@cache
def intersect(a, b) -> bool:
    ax, ay = a
    bx, by = b
    for i, (x1, y1) in enumerate(lines):
        x2, y2 = lines[(i + 1) % len(lines)]
        x1, x2 = [x1, x2] if x1 <= x2 else [x2, x1]
        y1, y2 = [y1, y2] if y1 <= y2 else [y2, y1]
        if x1 == x2:
            if ax < x1 < bx and ay < y2 and y1 < by:
                return True
        else:
            if ay < y1 < by and ax < x2 and x1 < bx:
                return True
    return False


for a, b in combinations(lines, 2):
    x1, x2, y1, y2 = list(chain(*list(zip(a, b))))
    x1, x2 = [x1, x2] if x1 <= x2 else [x2, x1]
    y1, y2 = [y1, y2] if y1 <= y2 else [y2, y1]
    if x1 == x2 or y1 == y2:
        continue
    area = (x2 - x1 + 1) * (y2 - y1 + 1)
    p1 = max(area, p1)

    if area <= p2:
        continue
    if (
        valid(x1, y1)
        and valid(x1, y2)
        and valid(x2, y1)
        and valid(x2, y2)
        and not intersect((x1, y1), (x2, y2))
    ):
        p2 = max(area, p2)

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
