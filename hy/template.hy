(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(print lines)

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
