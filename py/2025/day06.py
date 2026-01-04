import sys
import math

p1, p2 = 0, 0
D = sys.stdin.read()
lines = [line for line in D.split("\n") if line]

N = tuple(zip(*(line.split() for line in lines)))
for *nums, op in N:
    ints = [int(n) for n in nums]
    p1 += sum(ints) if op == "+" else math.prod(ints)

N = tuple(zip(*tuple(lines)))
fs, OP, R = True, "", 0
for i, (*nums, op) in enumerate(N):
    if fs and op != " ":
        OP = op
        R = 0 if OP == "+" else 1
        fs = not fs
    if all(d == " " for d in nums):
        fs = True
    else:
        v = int("".join(nums))
        if OP == "+":
            R += v
        else:
            R *= v
    if fs or i == len(N) - 1:
        p2 += R

print(f"Part 1: {p1}")
print(f"Part 2: {p2}")
