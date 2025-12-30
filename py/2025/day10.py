import sys
from collections import defaultdict
from functools import cache

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [line.split() for line in D.split("\n") if line]
# print(lines)
for lights, *buttons, joltages in lines:
    lights = list(lights[1:-1])
    buttons = [[int(b) for b in bs[1:-1].split(",")] for bs in buttons]
    joltages = [int(j) for j in joltages[1:-1].split(",")]

    S = defaultdict(list)
    for i in range(1 << len(buttons)):
        res = [0] * len(lights)
        pressed = [0] * len(buttons)
        for btn, bits in enumerate(buttons):
            if (i >> btn) & 1 == 1:
                for b in bits:
                    res[b] ^= 1
                pressed[btn] += 1
        S["".join("#" if i == 1 else "." for i in res)].append(pressed)

    p1 += min(sum(s) for s in S["".join(lights)])

    @cache
    def f2(T: tuple[int]) -> int:
        if all(v == 0 for v in T):
            return 0
        pressed = sys.maxsize
        lights = ["."] * len(T)
        for i, joltage in enumerate(T):
            if (joltage & 1) == 1:
                lights[i] = "#"
        for pre_pressed in S["".join(lights)]:
            joltage = list(T)
            for i, v in enumerate(pre_pressed):
                if v == 1:
                    for b in buttons[i]:
                        joltage[int(b)] -= 1
            if any(v < 0 for v in joltage):
                continue
            joltage = [v >> 1 for v in joltage]
            p = f2(tuple(joltage))
            if p != sys.maxsize:
                pressed = min(pressed, sum(pre_pressed) + 2 * p)
        return pressed

    p2 += f2(tuple(joltages))

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
