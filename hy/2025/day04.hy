(import sys)
(import itertools [count product])
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line (list line)))

(defn adjacent8 [c limit]
	(setv [row col] c
	      offsets [[-1 -1] [-1 0] [-1 1]
		       [0 -1]         [0 1]
		       [1 -1]  [1 0]  [1 1]])
	(lfor [dr dc] offsets
		:setv nr (+ row dr)
		:setv nc (+ col dc)
		:if (and (<= 0 nr (dec limit))
			 (<= 0 nc (dec limit)))
		#(nr nc)))

(for [i (count 0)]
	(setv curr p2)
	(for [[row col] (product (range (len lines)) (range (len lines)))]
		(when (= "@" (get lines row col))
			(setv cnt (len (lfor [r c] (adjacent8 [row col] (len lines))
					       :if (= "@" (get lines r c))
					       #(r c))))
			(when (< cnt 4)
				(if (= i 0)
					(+= p1 1)
					(do (setv (get lines row col) "x")
					    (+= p2 1))))))
	(when (and (> i 0) (= curr p2))
		(break)))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
