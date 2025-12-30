import sys
from functools import cache

p1, p2 = 0, 0
D = sys.stdin.read()
G = {}
lines = [line for line in D.split("\n") if line]
for line in lines:
    src, rest = line.split(":")
    G[src] = rest.split()


def f(x: str) -> int:
    if x == "out":
        return 1
    else:
        return sum(f(node) for node in G.get(x, []))


p1 = f("you")


@cache
def g(x: str, fft: bool, dac: bool) -> int:
    if x == "out":
        return 1 if fft and dac else 0
    else:
        return sum(
            g(node, node == "fft" or fft, node == "dac" or dac) for node in G.get(x, [])
        )


p2 = g("svr", False, False)

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
