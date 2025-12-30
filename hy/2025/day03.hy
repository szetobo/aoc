(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(defn pick [line k]
	(setv start 0
	      res [])
	(for [rem (range k 0 -1)]
		(setv [pos digit] [start 0])
		(for [p (range start (inc (- (len line) rem)))]
			(when (> (setx d (get line p)) digit)
				(setv [pos digit] [p d])))
		(.append res (str digit))
		(setv start (inc pos)))
	(-> (.join "" res) int))

(for [line lines]
	(setv digits (lfor ch (list line) (int ch)))
	(setv p1 (+ p1 (pick digits 2)))
	(setv p2 (+ p2 (pick digits 12))))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
