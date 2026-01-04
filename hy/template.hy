(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0 p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(print lines)

(print f"Part 1: {p1}")
(print f"Part 2: {p2}")
