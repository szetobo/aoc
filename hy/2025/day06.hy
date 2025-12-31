(import sys)
(import math)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(setv N (tuple (zip #* (lfor line lines (.split line)))))

(for [#(#* nums op) N]
	(setv ints (gfor n nums (int n)))
	(+= p1 (if (= op "+") (sum ints) (math.prod ints))))

(setv N (tuple (zip #* lines))
      fs True
      OP ""
      R 0)

(for [[i #(#* nums op)] (enumerate N)]
	(when (and fs (!= op " "))
		(setv OP op
		      R (if (= OP "+") 0 1)
		      fs (not fs)))
	(if (all (gfor d nums (= d " ")))
		(setv fs True)
		(do (setv v (int (.join "" nums)))
		    (if (= OP "+") (+= R v) (*= R v))))
	(when (or fs (= i (dec (len N))))
		(+= p2 R)))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
