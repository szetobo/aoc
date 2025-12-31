(import sys)
(import itertools [product])
(import collections [defaultdict])
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line (list line)))

(setv M (defaultdict int)
      n (len lines))

(for [[row col] (product (range (dec n)) (range (dec n)))]
  (setv ch (get lines row col))
  (when (or (= ch "|") (= ch "S"))
    (setv cnt (.get M #(row col) 1)
          char (get lines (inc row) col))
    (when (= char "^")
      (setv (get lines (inc row) (dec col)) "|")
      (+= (get M #((inc row) (dec col))) cnt)
      (setv (get lines (inc row) (inc col)) "|")
      (+= (get M #((inc row) (inc col))) cnt)
      (+= p1 1))
    (when (or (= char "|") (= char "."))
      (setv (get lines (inc row) col) "|")
      (+= (get M #((inc row) col)) cnt))))

(setv p2 (sum (gfor [col ch] (enumerate (get lines (dec n)))
                :if (= ch "|")
                (get M #((dec n) col)))))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
