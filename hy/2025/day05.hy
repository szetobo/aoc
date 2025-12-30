(import sys)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      parts (.split D "\n\n")
      R (lfor line (.splitlines (get parts 0)) (lfor r (.split line "-") (int r)))
      L (lfor line (.splitlines (get parts 1)) (int line)))

(setv p1 (sum (lfor n L (next (gfor [s e] R :if (<= s n e) 1) 0))))

(.sort R)

(setv res (ap-reduce (let [[s e] it [val lst] acc]
			     #((+ val (if (<= e lst) 0 (inc (- e (max s (inc lst))))))
			       (max lst e)))
		     R #(0 -1)))
(setv p2 (get res 0))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
