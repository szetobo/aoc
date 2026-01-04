(import sys)
(import math)
(import hyrule *)
(require hyrule *)

(setv p1 0 p2 0)

(setv D (.read sys.stdin))
(setv lines (lfor line (.split D "\n") :if line line))

(setv N (tuple (zip #* (lfor line lines (.split line)))))

(for [#(#* nums op) N]
  (setv ints (gfor n nums (int n)))
  (+= p1 (if (= op "+") (sum ints) (math.prod ints))))

(setv N (tuple (zip #* lines)))
(setv fs True)
(setv OP "")
(setv R 0)

(for [[i #(#* nums op)] (enumerate N)]
  (when (and fs (!= op " "))
      (do (setv OP op)
          (setv R (if (= OP "+") 0 1))
          (setv fs (not fs))))
  (if (all (gfor d nums (= d " ")))
      (setv fs True)
      (do (setv v (int (.join "" nums)))
          (if (= OP "+") (+= R v) (*= R v))))
  (when (or fs (= i (dec (len N))))
      (+= p2 R)))

(print f"Part 1: {p1}")
(print f"Part 2: {p2}")
