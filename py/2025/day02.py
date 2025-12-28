import sys


# def build_lps(s: str) -> list[int]:
#     x, n = 0, len(s)
#     lps = [0] * n
#     for i in range(1, n):
#         while x > 0 and s[i] != s[x]:
#             x = lps[x - 1]
#         if s[i] == s[x]:
#             x += 1
#         lps[i] = x
#     return lps


part1, part2 = 0, 0
while line := sys.stdin.readline().strip():
    pairs = [tuple(map(int, part.split("-"))) for part in line.split(",")]
    for n1, n2 in pairs:
        for i in range(n1, n2 + 1):
            s = str(i)
            n = len(s)
            if n % 2 == 0:
                n //= 2
                if s[:n] == s[n:]:
                    part1 += i
            if s in (s + s)[1:-1]:
                part2 += i
            # lps = build_lps(s)
            # longest = lps[n - 1]
            # period = n - longest
            # if longest > 0 and (n % period) == 0:
            #     if (longest // period) % 2 == 1:
            #         part1 += i
            #     part2 += i

print(f"The result for part 1: {part1}")
print(f"The result for part 2: {part2}")
