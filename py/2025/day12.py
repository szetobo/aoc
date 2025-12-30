import sys
import re

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]
S: list[list[list[str]]] = []
T: list[int] = []
for line in lines:
    if len(line) == 2:
        S.append([])
        T.append(0)
    elif re.match(r"[#.]+", line):
        S[-1].append(line.split())
        T[-1] = T[-1] + len([x for x in line if x == "#"])
    else:
        width, height, *rest = [int(x) for x in re.findall(r"\d+", line)]
        ttl_t = width * height
        min_p = (width / 3) * (height / 3)
        cnt_p = sum(rest)
        cnt_t = sum(v * T[i] for i, v in enumerate(rest))
        if cnt_p <= min_p and cnt_t <= ttl_t:
            p1 += 1

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
