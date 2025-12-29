import itertools
import math
import sys
from operator import itemgetter, truediv


class DSU:
    def __init__(self, n):
        self.parent = {}

    def find(self, i):
        if i not in self.parent:
            self.parent[i] = i
            return i
        if self.parent[i] != i:
            self.parent[i] = self.find(self.parent[i])
        return self.parent[i]

    def union(self, i, j):
        root_i, root_j = self.find(i), self.find(j)
        if root_i != root_j:
            self.parent[root_j] = root_i
            return True
        return False


p1, p2 = 0, 0
D = sys.stdin.read()
lines = [tuple(map(int, line.split(","))) for line in D.split("\n") if line]

E = []
for a, b in itertools.combinations(lines, 2):
    ax, ay, az = a
    bx, by, bz = b
    w = (ax - bx) ** 2 + (ay - by) ** 2 + (az - bz) ** 2
    E.append((a, b, w))
E.sort(key=itemgetter(2))

n = len(lines)
dsu = DSU(n)
for i in range(10 if n == 20 else n):
    a, b, _ = E[i]
    dsu.union(a, b)
M = {}
for i in lines:
    root = dsu.find(i)
    M[root] = M.get(root, 0) + 1
p1 = math.prod(sorted(M.values())[-3:])

dsu = DSU(n)
m = n
for a, b, _ in E:
    if dsu.union(a, b):
        m -= 1
        if m == 1:
            p2 = a[0] * b[0]
            break

print(f"The result for part 1: {p1}")
print(f"The result for part 2: {p2}")
