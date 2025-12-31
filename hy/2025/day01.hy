(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0
      pos 50)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(for [line lines]
	(setv d (if (= (get line 0) "L") -1 1)
	      num (int (cut line 1 None)))
	(for [i (range num)]
		(setv pos (% (+ pos d) 100))
		(when (= 0 pos) (+= p2 1)))
	(when (= 0 pos) (+= p1 1)))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
