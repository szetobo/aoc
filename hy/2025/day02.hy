(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line))

(for [line lines]
	(setv pairs (lfor part (.split line ",") (lfor pair (.split part "-") (int pair))))
	(for [[n1 n2] pairs]
		(for [i (range n1 (inc n2))]
			(setv s (str i)
			      n (len s))
			(when (= 0 (% n 2))
				(when (= (cut s 0 (// n 2)) (cut s (// n 2) None))
					(+= p1 i)))
			(when (in s (cut (+ s s) 1 -1))
				(+= p2 i)))))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
