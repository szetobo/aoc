import sys


def pick(s: str, n: int) -> int:
    res: list[str] = ["0"] * n
    sta: int = 0
    for i in range(n, 0, -1):
        p, d = sta, "0"
        for j in range(sta, len(s) - i + 1):
            if (v := s[j]) > d:
                p, d = j, v
        res[n - i] = d
        sta = p + 1
    return int("".join(res))


p1, p2 = 0, 0
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]
for line in lines:
    p1 += pick(line, 2)
    p2 += pick(line, 12)

print(f"Part 1: {p1}")
print(f"Part 2: {p2}")
